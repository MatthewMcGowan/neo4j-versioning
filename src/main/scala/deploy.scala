import com.typesafe.config.{Config, ConfigFactory, ConfigList, ConfigValue}
import deploymentHistory.deploymentHistoryParser
import model.{Environment, Script}

import scala.annotation.tailrec
import scala.io.Source.fromResource
import scala.io.StdIn.readLine

/**
  * Created by Matthew.McGowan on 13/06/2017.
  */
object deploy extends App {
  val conf = ConfigFactory.load()


  val historyJson = fromResource("deploymentHistory.json").getLines().mkString("\n")
  val historyOpt = deploymentHistoryParser.parse(historyJson)

  if (historyOpt.isEmpty) {
    println("History not present or corrupted. Exiting.")
    System.exit(-1)
  }
  val history = historyOpt.get

  printHistory(history)

  val env = getEnvironment(history)

  print(env)

  def printHistory(history: Seq[Environment]) = {
    println("Environment Status:")
    history.foreach(x =>
      println(s"- ${x.name} at ${x.address} is at V${x.history.last.version}"))
    println()
  }

  @tailrec
  def getEnvironment(history: Seq[Environment]): Environment = {
    println("Please enter environment name to update:")
    val input = readLine()

    val e = history.find(_.name == input)
    if (e.isEmpty) {
      println("Environment not found.")
      getEnvironment(history)
    }
    else
      e.get
  }

  @tailrec
  def askBoolean(question: String): Boolean = {
    println(question)
    val input = readLine().toLowerCase
    if (input == "y") true
    else if (input == "n") false
    else {
      println("Please respond either Y or N.")
      askBoolean(question)
    }
  }

  type EnvironmentConfiguration = (String, String)
}
