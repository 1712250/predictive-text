package function

class ProductHash extends HashFunction {
  def getHash(word: String, base: Int): Int = {
    var product = 1
    for (c <- word) yield product = (product * c.toInt) % base
    product % base
  }

  def getName: String = "Product Hash"

}
