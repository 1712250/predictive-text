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
  val directory = "/home/cpu11462-local/Desktop/scala/blogs"
  val injector: ServiceInjector = new BloomBKService
  val dict = injector.getDictionary

  def main(args: Array[String]): Unit = {
    loadDatasets
    // interactive

    val file = new File("datasets.txt")
    val bw = new BufferedWriter(new FileWriter(file))
    val text = dict.getSuggestion("", 1e6.toInt).mkString(" ")
    bw.write(text)
    bw.close()
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
      if (!Character.isLetter(c))
        return false;
    }
    return true
  }

  def loadDatasets() {
    val filepaths = getListOfFiles(directory)
    try {
      val futures: Future[Iterator[String]] =
        for (f <- filepaths) yield Future[Iterator[String]] {
          println(s"Processing $f...")
          val lines = Source.fromFile(f).getLines
          val words =
            lines.flatMap(line => line.split(" ").filter(word => isWord(word)))
          words
        }

      futures.foreach(f =>
        f onComplete {
          case Success(words) => words.foreach(w => dict.insert)
          case Failure(err) =>
            println("An error has occurred: " + err.getMessage)
        }
      )
      words.foreach(dict.insert)
    } catch {
      case e: Throwable => println(s"Error $e")
    }
  }

  def interactive() {
    println("*****************************************")
    println("*********   INTERACTIVE SHELL   *********")
    println("*****************************************")
    breakable(
      while (true) {
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
            case "i" => dict.insert(arr(1))
            case "c" => {
              if (dict.contains(arr(1))) println("Word may be exist!")
              else println("Word is not exist!")
            }
            case "s" => {
              print("Suggestions: ")
              dict.getSuggestion(arr(1), 5).foreach(w => print(s"$w "))
              println
            }
            case s => println(s"Unknown command: $s")
          }
        }
      }
    )
  }

}
