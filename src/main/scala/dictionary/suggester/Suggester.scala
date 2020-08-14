package dictionary.suggester

trait Suggester {
  def insert(word: String): Unit
  def getSuggestion(word: String): Seq[String]
  def transfer(another: Suggester): Unit
  def getName: String
}
