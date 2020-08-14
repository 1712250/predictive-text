package dictionary
import dictionary.searcher._
import dictionary.suggester._

class TwoDataStructuresDict extends Dictionary {
  private var searcher: Option[Searcher] = None
  private var suggester: Option[Suggester] = None

  def contains(word: String): Boolean =
    searcher match {
      case Some(s) => s.contains(word)
      case None    => throw UninitializedFieldError("Uninititalized Searcher!")
    }

  def insert(word: String): Unit = {
    searcher match {
      case Some(s) => s.insert(word)
      case None    => throw UninitializedFieldError("Uninititalized Searcher!")
    }
    suggester match {
      case Some(s) => s.insert(word)
      case None    => throw UninitializedFieldError("Uninititalized Suggester!")
    }
  }

  def getSuggestion(word: String, maximumSuggestion: Int): Seq[String] =
    suggester match {
      case Some(s) => s.getSuggestion(word).take(maximumSuggestion)
      case None    => throw UninitializedFieldError("Uninititalized Suggester!")
    }

  def getName: String = "Two Data Structures"

  def setSearcher(newSearcher: Searcher): Unit =
    searcher match {
      case Some(s) => s.transfer(newSearcher); searcher = Some(newSearcher);
      case None    => searcher = Some(newSearcher);
    }

  def setSuggester(newSuggester: Suggester): Unit =
    suggester match {
      case Some(s) => s.transfer(newSuggester); suggester = Some(newSuggester);
      case None    => suggester = Some(newSuggester);
    }

}
