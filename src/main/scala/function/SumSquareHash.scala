package function

class SumSquareHash extends HashFunction {
  def getHash(word: String, base: Int): Int = {
    var sum = 0
    for (c <- word) yield sum += c.toInt * c.toInt
    sum % base
  }

  def getName: String = "Sum Square Hash"

}
