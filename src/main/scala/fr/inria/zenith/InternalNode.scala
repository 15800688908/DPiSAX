package fr.inria.zenith

import com.typesafe.config.ConfigFactory

import scala.collection.mutable

/**
  * Created by leva on 20/07/2018.
  */
class InternalNode (nodeCard /* card */: Array[Int], childHash: mutable.HashMap[String /* word_to_card.card = nodeID */, SaxNode]  ) extends SaxNode {

  val config = AppConfig(ConfigFactory.load()) //is it good to define in class ? how is better to made available global configuration in class objects ?


  private def wordToCard (saxWord: Array[Int]) = (saxWord zip nodeCard).map { case (w, c) => w >> (config.maxCardSymb - c) }

  override def insert(saxWord: Array[Int] , tsId: Int ): Unit  = {

    //val word = wordToCard(saxWord).mkString("\"",",","\"")
    val nodeID : String  = (wordToCard(saxWord) zip nodeCard).map{case (w,c) => s"$w.$c"}.mkString("_")

    if (childHash.contains(nodeID)){

      var child = childHash(nodeID)
      if (child.isInstanceOf[TerminalNode] && child.shallSplit) {
        child = child.split()
        childHash(nodeID) = child
      }
      child.insert(saxWord, tsId)
    }
    else { // when TerminalNode doesn't exist
      var newTermNode = new TerminalNode(Array.empty, nodeCard, config.basicSplitBalance(nodeCard), wordToCard(saxWord))
      this.childHash += nodeID -> newTermNode
      newTermNode.insert(saxWord, tsId)
    }

  }

  override def shallSplit : Boolean =  false

  override def split () : InternalNode  = this

  override def toJSON (fsURI: String) : String =  "{\"_CARD_\" :" + nodeCard.mkString("\"",",","\"") + ", " + childHash.map(child => child._1.mkString("\"","","\"") + ":" + child._2.toJSON(fsURI) ).mkString(",") + "}"

  override def approximateSearch(saxWord: Array[Int]) : Array[(Array[Int],Int)]  = {

   // val word = wordToCard(saxWord).mkString("\"",",","\"")
    val nodeID : String  = (wordToCard(saxWord) zip nodeCard).map{case (w,c) => s"$w.$c"}.mkString("_")
  //  println("nodeID = " + nodeID)
/*
    try{
      childHash(word).approximateSearch(saxWord)
    } catch {
      case e: Exception => println("exception caught:" + e)
    }
    */

    if (childHash.contains(nodeID))
   {
      childHash(nodeID).approximateSearch(saxWord)
   }
    else if (childHash.size == 1) {

      childHash.head._2.fullSearch  // fuul search on the tree from current node and return all TerminalNodes
    }
    else Array.empty
  }

  override def boundedSearch(paa: Array[Float], bound: Float, tsLength: Int): Array[(Array[Int], Int)] =
    childHash.map( _._2.boundedSearch(paa, bound, tsLength) ).reduce(_++_)

  def fullSearch : Array[(Array[Int],Int)] = childHash.flatMap(_._2.fullSearch).toArray

  def partTreeSplit (nodeToSplitID: String) : Unit = {

    if (childHash.contains(nodeToSplitID) && childHash(nodeToSplitID).isInstanceOf[TerminalNode] ){
      var child = childHash(nodeToSplitID)
      child = child.split()
      childHash(nodeToSplitID) = child
      }
    else childHash.foreach(_._2.partTreeSplit(nodeToSplitID))
  }

  override def partTable  : Array[(String,Array[Int],Int)] = childHash.flatMap(_._2.partTable).toArray
}



