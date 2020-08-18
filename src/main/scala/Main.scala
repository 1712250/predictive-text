import dictionary._
import dictionary.searcher._
import dictionary.suggester._
import dictionary.service._

import scala.xml._
import scala.io.Source
import scala.concurrent.Future
import scala.util.control.Breaks._
import scala.util.{Success, Failure}

import java.io._

object Main {
  val directory = "./blogs"
  val datasetsFilename = "./datasets.txt"
  val injector: ServiceInjector = new BloomBKService
  var dict = injector.getDictionary

  def main(args: Array[String]): Unit = {
    // dict = new TrieDict
    // preprocess
    loadDatasets
    interactive
  }

  def getListOfFiles(dir: String): List[String] = {
    val file = new File(dir)
    file.listFiles
      .filter(_.isFile)
      .map(_.getPath)
      .toList
  }

  def isWord(s: String): Boolean = {
    if (s.isEmpty)
      return false
    for (c <- s) {
      if (c < 'A' || c > 'z' || (c > 'Z' && c < 'a'))
        return false;
    }
    return true
  }

  def preprocess() {
    val dir = getListOfFiles(directory)
    for (f <- dir) {
      try {
        println(s"Processing $f...")
        val lines = Source.fromFile(f).getLines
        val words =
          lines.flatMap(line => line.split(" ").filter(word => isWord(word)))
        words.foreach(dict.insert)
      } catch {
        case e: Throwable =>
          println(s"File $f: Error while parsing words. Exception: $e")
      }
    }
    val file = new File(datasetsFilename)
    val bw = new BufferedWriter(new FileWriter(file))
    val text = dict.getSuggestion("", 1e6.toInt).mkString(" ")
    bw.write(text)
    bw.close()
  }

  def loadDatasets() {
    try {
      print("Loading datasets...")
      val lines = Source.fromFile(datasetsFilename).getLines
      lines.foreach(line => line.split(" ").foreach(dict.insert))
      println(" Done")
    } catch {
      case e: Throwable =>
        println(s"Error while parsing words. Exception: $e")
    }
  }

  def interactive() {
    println("*****************************************")
    println("*********   INTERACTIVE SHELL   *********")
    println("*****************************************")
    breakable(
      while (true) {
        print("> ")
        val word = readLine()
        val arr = word.split(" ")
        arr(0) match {
          case "exit" => break
          case "h"    =>
          case _      =>
        }

        breakable {
          if (arr.length != 2) {
            println("Incorrect syntax!")
            break
          }

          arr(0) match {
            case "i" =>
              benchmark(() => {
                dict.insert(arr(1))
                print("Inserted")
              })
            case "c" =>
              benchmark(() => {
                if (dict.contains(arr(1))) print("Word may be exist!")
                else print("Word is not exist!")
              })
            case "s" =>
              benchmark(() => {
                print("Suggestions: ")
                dict.getSuggestion(arr(1), 5).foreach(w => print(s"$w "))
              })
            case s => println(s"Unknown command: $s")
          }
        }
      }
    )
  }

  def benchmark(f: () => Unit) {
    val t = System.nanoTime
    f()
    val duration = (System.nanoTime - t) / 1e6d
    println(s"\t -- in $duration milisecond(s)")
  }
}
