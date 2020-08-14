package dictionary

trait Dictionary {
  def insert(word: String): Unit
  def contains(word: String): Boolean
  def getSuggestion(word: String, maximumSuggestion: Int): Seq[String]
  def getName: String
}
