package function

trait HashFunction {
  def getHash(word: String, base: Int): Int
  def getName: String
}
