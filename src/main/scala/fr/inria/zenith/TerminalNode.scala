package fr.inria.zenith

import com.typesafe.config.ConfigFactory

import scala.collection.mutable


class TerminalNode (var tsIDs: Array[(Array[Int],Long)], nodeCard: Array[Int], wordToCard: Array[Int]) extends SaxNode(nodeCard, wordToCard) {

  val config = AppConfig(ConfigFactory.load())
  var splitBalance : Array[Array[Int]] = config.basicSplitBalance(nodeCard)
  val nodeID : String = config.nodeID(wordToCard, nodeCard)
  var maxCardStep : Int = config.maxCardSymb

  /**  Array[(elem_to_split_position, cardinality_to_split_on)] **/
  def splitCandList : List[(Int,Int,Int)] = splitBalance.map(_.map(math.abs(_)).zipWithIndex.dropWhile(_._1 >= tsIDs.length)).zipWithIndex.filter(_._1.nonEmpty).map(v => (v._1.head, v._2)).filter(v => nodeCard(v._2) < config.maxCardSymb).toList.sortBy(r => r._1._2 * tsIDs.length + r._1._1).map(v => (v._2,v._1._2,v._1._1)).filter(_._2 < maxCardStep)




  override def insert(saxWord: Array[Int] , tsId: Long): Unit  = {
    val wordToCardNext = (saxWord zip nodeCard).map { case (w, c) => for (i <- c until config.maxCardSymb) yield { (w >> (config.maxCardSymb - i - 1) & 0XFF).toByte}  }
    splitBalance = splitBalance.zip(wordToCardNext).map(v => v._1.zip(v._2).map(v => v._1 + ((v._2 % 2) * 2 - 1)))
    tsIDs = tsIDs :+ (saxWord, tsId)
  }

  override def shallSplit : Boolean =  tsIDs.length >= config.threshold  && splitCandList.nonEmpty

  override def split() : SaxNode = {

    val elemToSplit  = splitCandList.head

    val cardStep = elemToSplit._2 + 1

    val dw = splitBalance(elemToSplit._1).take(elemToSplit._2).map(v => if (v>0) 1 else 0).reverse.zipWithIndex.map(v => v._1 << v._2).sum

    val newNodeCard = nodeCard.updated(elemToSplit._1, nodeCard(elemToSplit._1) + cardStep)
    val newWordToCard = (0 to 1).map(v => wordToCard.updated(elemToSplit._1, ((wordToCard(elemToSplit._1) << (cardStep-1)) + dw) * 2 + v))
    val newTermNodes = newWordToCard.map(v => new TerminalNode(Array.empty, newNodeCard, v)).toArray

    tsIDs.foreach(ts => newTermNodes((ts._1(elemToSplit._1) >> ((config.maxCardSymb - newNodeCard(elemToSplit._1)) & 0XFF).toByte) % 2).insert(ts._1, ts._2))

    var childHash = new mutable.HashMap[String, SaxNode]()
    childHash ++= newTermNodes.map(node => node.nodeID -> node)

    var newInternalNode = new InternalNode(newNodeCard, childHash, nodeCard, wordToCard)
    newInternalNode
  }

  override def toJSON (fsURI: String) : String = {
    tsToFile(fsURI)
    "{\"_CARD_\" :" + nodeCard.mkString("\"", ",", "\"") + ", " + "\"_FILE_\" :" + "\"" + nodeID + "\"" + ", \"_NUM_\":" + tsIDs.length + "}"
  }

  override def approximateSearch(saxWord: Array[Int], paa: Array[Float]) : Array[Long] = tsIDs.map(_._2)

  override def boundedSearch(paa: Array[Float], bound: Float, tsLength: Int): Array[Long] =
      tsIDs.filter( t => config.mindist(paa, t._1, Array.fill[Int](config.wordLength)(config.maxCardSymb), tsLength) <= bound ).map(_._2)

  def fullSearch : Array[Long] = tsIDs.map(_._2)

  def partTreeSplit (node: String) : Unit  =  if (node == nodeID) this.split()

  override def partTable  : Array[ (String,Array[Int],Int)] = {
    maxCardStep = 1
    Array((nodeID, nodeCard,  (tsIDs.length - splitCandList.map(_._3).headOption.getOrElse(tsIDs.length))/2))
  }


  def tsToFile (fsURI: String) =  {
   val writer = Utils.setWriter(fsURI, config.workDir + nodeID)
     tsIDs.foreach(t => writer.write (t._1.mkString(",") + " " + t._2 + "\n") )
     writer.close
  }


}


