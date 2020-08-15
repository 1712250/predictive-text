package dictionary.service

import dictionary._
import dictionary.searcher._
import dictionary.suggester._

class BloomBKService extends ServiceInjector {
  def getDictionary(): Dictionary = {
    val searcher = SearcherFactory.getSearcher("bloom", 1e6.toInt)
    val suggester = SuggesterFactory.getSuggester("bktree", 12, 12)
    val dict = new TwoDataStructuresDict
    dict.setSearcher(searcher)
    dict.setSuggester(suggester)
    dict
  }
}
