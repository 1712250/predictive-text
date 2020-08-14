package dictionary.searcher

object SearcherFactory {
  def getSearcher(name: String, params: Int*): Searcher =
    name match {
      case "bloom"  => new BloomFilter(params(0))
      case "cuckoo" => new CuckooFilter(params(0), params(1))
      case _        => new BloomFilter(1e6.toInt)
    }
}
