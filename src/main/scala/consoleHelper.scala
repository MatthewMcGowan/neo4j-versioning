import model.Environment

import scala.annotation.tailrec
import scala.io.StdIn.readLine

/**
  * Created by Matthew.McGowan on 28/06/2017.
  */
object consoleHelper {
  def printHistory(history: Seq[Environment]) = {
    println("Environment Status:")
    history.foreach(printEnvironmentStatus)
    println()
  }

  def printEnvironmentStatus(env: Environment) =
    println(s"- ${env.name} at ${env.address} is at V${env.history.last.version}")

  def askBoolean(question: String): Boolean = {
    val input = askForValue(question, Seq("Y", "N"))
    input == "y"
  }

  @tailrec
  def askForValue(question: String, allowableValues: Seq[String]): String = {
    println(question)
    val input = readLine().toLowerCase
    if(allowableValues.contains(input)) input
    else {
      println(s"Please respond with one of the following: $allowableValues")
      askForValue(question, allowableValues)
    }
  }
}
