package dictionary.service

import dictionary._
import dictionary.searcher._
import dictionary.suggester._

class CuckooBKService extends ServiceInjector {
  def getDictionary(): Dictionary = {
    val searcher = SearcherFactory.getSearcher("cuckoo", 1e6.toInt, 4)
    val suggester = SuggesterFactory.getSuggester("bktree", 12, 3)
    val dict = new TwoDataStructuresDict
    dict.setSearcher(searcher)
    dict.setSuggester(suggester)
    dict
  }
}
