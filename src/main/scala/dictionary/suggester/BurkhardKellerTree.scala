package dictionary.suggester

import scala.collection.mutable.ArrayBuffer
import scala.math._

class BurkhardKellerTree(
    private val maximumWordLen: Int,
    private val tolerance: Int
) extends Suggester {
  private class Node(var word: String = "") {
    var next = new Array[Option[Node]](2 * maximumWordLen)
  }

  private val root: Node = new Node()

  def insert(word: String): Unit = {
    if (root.word.isEmpty) {
      root.word = word
      return
    } else {
      addNode(root, word)
    }
  }

  def getSuggestion(word: String): Seq[String] = {
    val similarWords = getSimilarWords(root, word)
    similarWords.sortWith((t1, t2) => t1._2 < t2._2).map(t => t._1).toSeq
  }

  private def getSimilarWords(
      current: Node,
      word: String
  ): ArrayBuffer[Tuple2[String, Int]] = {

    if (current.word.isEmpty)
      return ArrayBuffer.empty

    val similarWords = new ArrayBuffer[Tuple2[String, Int]]
    val dist = calcDistance(word, current.word)
    if (dist <= tolerance)
      similarWords += Tuple2(current.word, dist)

    var ndist = math.max(dist - tolerance, 1)
    val maxdist = math.min(dist + tolerance, 2 * maximumWordLen)

    try {
      while (ndist < maxdist) {
        current.next(ndist) match {
          case Some(node) => similarWords ++= getSimilarWords(node, word)
          case _          =>
        }
        ndist += 1
      }
    } catch {
      case e: Throwable =>
        println(
          s"$word and ${current.word}: $dist"
        )
    }
    similarWords

  }

  def transfer(another: Suggester): Unit = {}

  def getName: String = "Burkhard Keller Tree"

  private def calcDistance(word1: String, word2: String): Int = {
    val sa = word1.toLowerCase.toCharArray
    val sb = word2.toLowerCase.toCharArray
    val m = sa.length
    val n = sb.length
    val dp = Array.fill[Int](m + 1, n + 1)(elem = 0)

    for (i <- 0 to m) dp(i)(0) = i
    for (j <- 0 to n) dp(0)(j) = j

    for (i <- 1 to m)
      for (j <- 1 to n) {
        dp(i)(j) = dp(i - 1)(j - 1)
        if (sa(i - 1) != sb(j - 1))
          dp(i)(j) = 1 + min(dp(i)(j), min(dp(i - 1)(j), dp(i)(j - 1)))
      }
    dp(m)(n)
  }

  private def addNode(current: Node, word: String): Unit = {
    val distance = calcDistance(word, current.word)
    if (distance == 0 || distance >= 2 * maximumWordLen) return
    try {
      current.next(distance) match {
        case Some(node) => addNode(node, word)
        case _          => current.next(distance) = Some(new Node(word))
      }
    } catch {
      case e: Throwable =>
        println(
          s"$word and ${current.word}: $distance"
        )
    }
  }
}
