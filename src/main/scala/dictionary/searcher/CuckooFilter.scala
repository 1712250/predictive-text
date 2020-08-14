package dictionary.searcher
import function._
import scala.util.Random
import scala.util.control.Breaks._

class CuckooFilter(
    private val maximumEntries: Int,
    private val maximumKick: Int
) extends Searcher {

  private val bucket: Array[Array[Byte]] =
    Array.fill[Byte](maximumEntries, 2)(elem = -1)
  private val hashFuncs: Vector[HashFunction] =
    Vector(new Base26Hash, new ProductHash, new SumSquareHash)
  private val rand: Random = new Random

  def insert(word: String): Unit = {
    var (fingerprint, i1, i2) = getCuckooParams(word)

    if (bucket(i1).contains(-1))
      bucket(i1) = bucket(i1).map(v => if (v == -1) fingerprint else v)
    else
      breakable {
        for (_ <- Range(1, maximumKick)) {
          if (bucket(i2).contains(-1)) {
            bucket(i2) = bucket(i2).map(v => if (v == -1) fingerprint else v)
            break
          }

          val i = rand.nextInt(2)
          val f = bucket(i2)(i)
          bucket(i2)(i) = fingerprint
          fingerprint = f

          i2 = (i2 ^ hashFuncs(2).getHash(
            fingerprint.toString,
            maximumEntries
          )) % maximumEntries
        }
      }
  }

  def contains(word: String): Boolean = {
    val (fingerprint, i1, i2) = getCuckooParams(word)
    bucket(i1).contains(fingerprint) || bucket(i2).contains(fingerprint)
  }

  def transfer(another: Searcher): Unit = {
    println(
      "Warning: Cuckoo Filter cannot transfer its data to another Searcher!"
    )
  }

  private def getCuckooParams(word: String): Tuple3[Byte, Int, Int] = {
    val f = hashFuncs(0).getHash(word, Byte.MaxValue + 1).toByte
    val i1 = hashFuncs(1).getHash(word, maximumEntries)
    val i2 =
      (i1 ^ hashFuncs(2).getHash(f.toString, maximumEntries)) % maximumEntries
    (f, i1, i2)
  }

  def getName: String = "Cuckoo Filter"
}
