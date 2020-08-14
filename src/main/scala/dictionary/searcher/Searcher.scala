package dictionary.searcher

trait Searcher {
  def insert(word: String): Unit
  def contains(word: String): Boolean
  def transfer(another: Searcher): Unit
  def getName: String
}
