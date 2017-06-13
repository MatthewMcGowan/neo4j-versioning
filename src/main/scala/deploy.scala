import com.typesafe.config.ConfigFactory
import deploy.history
import model.Environment

import scala.annotation.tailrec
import scala.io.Source.fromResource
import scala.io.StdIn.readLine

/**
  * Created by Matthew.McGowan on 13/06/2017.
  */
object deploy extends App {
  val conf = ConfigFactory.load()

  val historyJson = fromResource("deploymentHistory.json").getLines().mkString("\n")
  val history = deploymentHistoryParser.parse(historyJson)

  if (history.isEmpty) {
    println("History not present or corrupted. Exiting.")
    System.exit(-1)
  }

  val envName = getEnvironmentName(history.get)

  if(!history.get.exists(_.name == envName)) {
    val question = "There is no history for this environment. " +
      "Confirm this is a new environment and you wish to proceed [Y/N]"
    if(!askBoolean(question)) System.exit(0)
  }

  


  @tailrec
  def getEnvironmentName(history: Seq[Environment]): String = {
    println("Please enter environment name to update:")
    val input = readLine()

    if(input.isEmpty) getEnvironmentName(history)
    else input
  }

  @tailrec
  def askBoolean(question: String): Boolean = {
    println(question)
    val input = readLine().toLowerCase
    if(input == "y") true
    else if(input == "n") false
    else {
      println("Please respond either Y or N.")
      askBoolean(question)
    }
  }
}
