package dictionary

import scala.collection.mutable._
import scala.util.control.Breaks._

class TrieDict extends Dictionary {
  private class Node {
    var hasWord = false
    val children = HashMap.empty[Char, Node]
  }

  private val root = new Node

  def insert(word: String): Unit = {
    var node = root
    for (c <- word) {
      if (!node.children.contains(c))
        node.children += (c -> new Node)
      node = node.children(c)
    }
    node.hasWord = true
  }

  def contains(word: String): Boolean = {
    var node = root
    for (c <- word) {
      if (!node.children.contains(c))
        return false
      node = node.children(c)
    }
    node.hasWord
  }

  def getSuggestion(word: String, maximumSuggestion: Int): Seq[String] = {
    var node = root
    for (c <- word) {
      if (!node.children.contains(c))
        return Seq.empty
      node = node.children(c)
    }

    val suggestions = MutableList.empty[String]
    val queue = Queue.empty[Tuple2[String, Node]]
    queue += (word -> node)

    var count = 0
    if (node.hasWord) {
      suggestions += word
      count += 1
    }

    while (count < maximumSuggestion && !queue.isEmpty) {
      var (word, node) = queue.dequeue
      breakable(for (t <- node.children) {
        val (curChar, curNode) = t
        if (curNode.hasWord) {
          suggestions += word + curChar
          count += 1
          if (count == maximumSuggestion) break
        }
        queue += (word + curChar -> curNode)
      })
    }
    suggestions
  }

  def getName: String = "Trie"
}
