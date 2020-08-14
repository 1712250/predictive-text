package function

class SumHash extends HashFunction {
  def getHash(word: String, base: Int): Int = {
    var sum = 0
    for (c <- word) yield sum += c.toInt
    sum % base
  }

  def getName: String = "Sum Hash"

}
