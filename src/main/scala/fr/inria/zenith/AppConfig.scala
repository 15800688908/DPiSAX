package fr.inria.zenith

import com.typesafe.config.Config
import scala.math._

/**
  * Created by leva on 23/07/2018.
  */
case class AppConfig (config: Config) {

  val workDir =  "/Users/leva/Documents/dpisax_res/"

  val maxCardSymb = config.getInt("maxCardSymb")
  val wordLength: Integer = config.getInt("wordLength")
  val threshold = config.getInt("threshold")

  val topk = 10
  val tsFilePath = config.getString("tsFilePath")
  val firstCol = config.getInt("firstCol")
  val numPart = config.getInt("numPart") //TODO parameter = number of workers/cores
 // val executors = sc.getExecutorMemoryStatus.size
 // val coresPerEx = sc.getConf.getInt("spark.executor.cores", 8)

  val sampleSize = config.getDouble("sampleSize")

  val queryFilePath = config.getString("queryFilePath")

//  val someOtherSetting = config.getString("some_other_setting")
  def basicCard = Array.fill[Int](wordLength)(1)
  def zeroArray =  Array.fill[Int](wordLength)(0)
  //TODO breakpoints read from file
  //breakpoints map card 9
  val breakpoints = Array(-2.8856, -2.6601, -2.5205, -2.4176, -2.3352, -2.2662, -2.2066, -2.1539, -2.1066, -2.0635, -2.024, -1.9874, -1.9533, -1.9214, -1.8912, -1.8627, -1.8357, -1.8099, -1.7853, -1.7617, -1.739, -1.7172, -1.6962, -1.6759, -1.6563, -1.6373, -1.6189, -1.601, -1.5836, -1.5667, -1.5502, -1.5341, -1.5184, -1.5031, -1.4881, -1.4735, -1.4591, -1.4451, -1.4313, -1.4178, -1.4045, -1.3915, -1.3788, -1.3662, -1.3539, -1.3417, -1.3298, -1.318, -1.3064, -1.295, -1.2838, -1.2727, -1.2618, -1.251, -1.2404, -1.2299, -1.2195, -1.2093, -1.1992, -1.1892, -1.1793, -1.1695, -1.1599, -1.1503, -1.1409, -1.1316, -1.1223, -1.1132, -1.1041, -1.0952, -1.0863, -1.0775, -1.0688, -1.0602, -1.0516, -1.0432, -1.0348, -1.0264, -1.0182, -1.01, -1.0019, -0.99382, -0.98583, -0.9779, -0.97003, -0.96222, -0.95447, -0.94678, -0.93915, -0.93156, -0.92403, -0.91656, -0.90913, -0.90175, -0.89443, -0.88715, -0.87991, -0.87273, -0.86558, -0.85848, -0.85143, -0.84442, -0.83744, -0.83051, -0.82362, -0.81677, -0.80995, -0.80317, -0.79643, -0.78973, -0.78306, -0.77642, -0.76982, -0.76325, -0.75672, -0.75022, -0.74374, -0.7373, -0.73089, -0.72451, -0.71816, -0.71184, -0.70555, -0.69928, -0.69305, -0.68683, -0.68065, -0.67449, -0.66836, -0.66225, -0.65616, -0.6501, -0.64407, -0.63806, -0.63207, -0.6261, -0.62015, -0.61423, -0.60833, -0.60245, -0.59659, -0.59075, -0.58493, -0.57913, -0.57335, -0.56759, -0.56185, -0.55613, -0.55042, -0.54473, -0.53906, -0.53341, -0.52777, -0.52215, -0.51655, -0.51097, -0.5054, -0.49984, -0.4943, -0.48878, -0.48327, -0.47777, -0.47229, -0.46683, -0.46137, -0.45593, -0.45051, -0.4451, -0.4397, -0.43431, -0.42894, -0.42358, -0.41823, -0.41289, -0.40756, -0.40225, -0.39695, -0.39166, -0.38638, -0.38111, -0.37585, -0.3706, -0.36536, -0.36013, -0.35491, -0.3497, -0.3445, -0.33931, -0.33413, -0.32896, -0.32379, -0.31864, -0.31349, -0.30835, -0.30322, -0.2981, -0.29299, -0.28788, -0.28278, -0.27769, -0.27261, -0.26753, -0.26246, -0.25739, -0.25234, -0.24729, -0.24224, -0.2372, -0.23217, -0.22714, -0.22212, -0.21711, -0.2121, -0.20709, -0.20209, -0.1971, -0.19211, -0.18713, -0.18215, -0.17717, -0.1722, -0.16723, -0.16227, -0.15731, -0.15236, -0.1474, -0.14246, -0.13751, -0.13257, -0.12764, -0.1227, -0.11777, -0.11284, -0.10792, -0.10299, -0.098072, -0.093154, -0.088238, -0.083324, -0.078412, -0.073503, -0.068594, -0.063688, -0.058783, -0.053879, -0.048977, -0.044076, -0.039176, -0.034277, -0.029379, -0.024481, -0.019584, -0.014688, -0.0097917, -0.0048958, 0, 0.0048958, 0.0097917, 0.014688, 0.019584, 0.024481, 0.029379, 0.034277, 0.039176, 0.044076, 0.048977, 0.053879, 0.058783, 0.063688, 0.068594, 0.073503, 0.078412, 0.083324, 0.088238, 0.093154, 0.098072, 0.10299, 0.10792, 0.11284, 0.11777, 0.1227, 0.12764, 0.13257, 0.13751, 0.14246, 0.1474, 0.15236, 0.15731, 0.16227, 0.16723, 0.1722, 0.17717, 0.18215, 0.18713, 0.19211, 0.1971, 0.20209, 0.20709, 0.2121, 0.21711, 0.22212, 0.22714, 0.23217, 0.2372, 0.24224, 0.24729, 0.25234, 0.25739, 0.26246, 0.26753, 0.27261, 0.27769, 0.28278, 0.28788, 0.29299, 0.2981, 0.30322, 0.30835, 0.31349, 0.31864, 0.32379, 0.32896, 0.33413, 0.33931, 0.3445, 0.3497, 0.35491, 0.36013, 0.36536, 0.3706, 0.37585, 0.38111, 0.38638, 0.39166, 0.39695, 0.40225, 0.40756, 0.41289, 0.41823, 0.42358, 0.42894, 0.43431, 0.4397, 0.4451, 0.45051, 0.45593, 0.46137, 0.46683, 0.47229, 0.47777, 0.48327, 0.48878, 0.4943, 0.49984, 0.5054, 0.51097, 0.51655, 0.52215, 0.52777, 0.53341, 0.53906, 0.54473, 0.55042, 0.55613, 0.56185, 0.56759, 0.57335, 0.57913, 0.58493, 0.59075, 0.59659, 0.60245, 0.60833, 0.61423, 0.62015, 0.6261, 0.63207, 0.63806, 0.64407, 0.6501, 0.65616, 0.66225, 0.66836, 0.67449, 0.68065, 0.68683, 0.69305, 0.69928, 0.70555, 0.71184, 0.71816, 0.72451, 0.73089, 0.7373, 0.74374, 0.75022, 0.75672, 0.76325, 0.76982, 0.77642, 0.78306, 0.78973, 0.79643, 0.80317, 0.80995, 0.81677, 0.82362, 0.83051, 0.83744, 0.84442, 0.85143, 0.85848, 0.86558, 0.87273, 0.87991, 0.88715, 0.89443, 0.90175, 0.90913, 0.91656, 0.92403, 0.93156, 0.93915, 0.94678, 0.95447, 0.96222, 0.97003, 0.9779, 0.98583, 0.99382, 1.0019, 1.01, 1.0182, 1.0264, 1.0348, 1.0432, 1.0516, 1.0602, 1.0688, 1.0775, 1.0863, 1.0952, 1.1041, 1.1132, 1.1223, 1.1316, 1.1409, 1.1503, 1.1599, 1.1695, 1.1793, 1.1892, 1.1992, 1.2093, 1.2195, 1.2299, 1.2404, 1.251, 1.2618, 1.2727, 1.2838, 1.295, 1.3064, 1.318, 1.3298, 1.3417, 1.3539, 1.3662, 1.3788, 1.3915, 1.4045, 1.4178, 1.4313, 1.4451, 1.4591, 1.4735, 1.4881, 1.5031, 1.5184, 1.5341, 1.5502, 1.5667, 1.5836, 1.601, 1.6189, 1.6373, 1.6563, 1.6759, 1.6962, 1.7172, 1.739, 1.7617, 1.7853, 1.8099, 1.8357, 1.8627, 1.8912, 1.9214, 1.9533, 1.9874, 2.024, 2.0635, 2.1066, 2.1539, 2.2066, 2.2662, 2.3352, 2.4176, 2.5205, 2.6601, 2.8856)

  def nodeID(wordToCard: Array[Int], nodeCard: Array[Int]) : String  = (wordToCard zip nodeCard).map{case (w,c) => s"$w.$c"}.mkString("_")
  def basicSplitBalance (nodeCard: Array[Int]): Array[Array[Int]] = nodeCard.map(v => Array.fill[Int](maxCardSymb - v)(0))    //Array.fill[Array[Int]](wordLength)(Array.fill[Int](maxCardSymb)(0))

  def normalize(ts: Array[Float]) : Array[Float] = {
    val mean = ts.sum / ts.length
    val stdev = sqrt( ts.map(x => x * x).sum / ts.length - mean * mean ).toFloat
    ts.map( x => (x - mean) / stdev )
  }

  def tsToPAAandSAX(ts: Array[Float]) : (Array[Float], Array[Int]) = {
    // ts must be normalized
    val segmentSize = ts.length / wordLength
    val numExtraSegments = ts.length % wordLength
    val sliceBorder = (wordLength - numExtraSegments) * segmentSize
    val paa = (ts.slice(0, sliceBorder).sliding(segmentSize, segmentSize) ++ ts.slice(sliceBorder, ts.length).sliding(segmentSize+1, segmentSize+1)).map(t => t.sum / t.length).toArray

    val sax = paa.map(t => breakpoints.indexWhere(t <= _)).map(t => if (t == -1) breakpoints.length else t).toArray

    (paa, sax)
  }

  def tsToSAX(ts: Array[Float]) : Array[Int] = tsToPAAandSAX(ts)._2

  def mindist(paa: Array[Float], wordToCard: Array[Int], card: Array[Int], tsLength: Int) : Float = {
    val saxBounds = (wordToCard.iterator zip card.iterator).map{ case(w, c) => ( (w << (maxCardSymb - c)) - 1, ((w + 1) << (maxCardSymb - c)) - 1 )}

    val symDistSq = (paa.iterator zip saxBounds).map{ case(paaValue, (saxLower, saxUpper)) =>
      if (saxLower >= 0 && paaValue < breakpoints(saxLower))
        pow(paaValue - breakpoints(saxLower), 2).toFloat
      else if (saxUpper < breakpoints.length && paaValue > breakpoints(saxUpper))
        pow(paaValue - breakpoints(saxUpper), 2).toFloat
      else 0 }

    sqrt(symDistSq.sum / wordLength * tsLength).toFloat
  }

  def distance(xs: Array[Float], ys: Array[Float]) : Float =
    sqrt((xs zip ys).map { case (x,y) => pow(y-x, 2)}.sum).toFloat

}
