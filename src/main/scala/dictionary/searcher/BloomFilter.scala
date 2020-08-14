package dictionary.searcher
import function._

class BloomFilter(private val maximumEntries: Int) extends Searcher {
  private val filter: Array[Byte] = Array.ofDim[Byte](maximumEntries)
  private val hashFuncs: List[HashFunction] =
    List(new ProductHash(), new SumHash(), new SumSquareHash())

  def insert(word: String): Unit = {
    for (hf <- hashFuncs) {
      val hash = hf.getHash(word, maximumEntries * 8)
      val i = hash / 8
      val j = hash % 8
      filter(i) = (filter(i).toByte | (1 << j).toByte).toByte
    }
  }

  def contains(word: String): Boolean = {
    for (hf <- hashFuncs) {
      val hash = hf.getHash(word, maximumEntries * 8)
      val i = hash / 8
      val j = hash % 8
      if (((filter(i) >> j) & 1) != 1)
        return false;
    }
    return true
  }

  def transfer(another: Searcher): Unit = {
    println(
      "Warning: Bloom Filter cannot transfer its data to another Searcher!"
    )
  }

  def getName: String = "Bloom Filter"
}
