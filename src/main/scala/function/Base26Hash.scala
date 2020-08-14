package function

class Base26Hash extends HashFunction {
  def getHash(word: String, base: Int): Int = {
    var hash = 1
    for (c <- word.toLowerCase) yield hash = (hash * 26 + c.toInt) % base
    hash % base
  }

  def getName: String = "Base26 Hash"

}
