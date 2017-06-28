import java.io.File

import com.typesafe.config.ConfigFactory
import deploymentHistory.deploymentHistoryParser
import model.{DeploymentCommand, DeploymentType, Environment}

import scala.annotation.tailrec
import scala.io.Source.fromResource
import scala.io.StdIn.readLine

/**
  * Created by Matthew.McGowan on 13/06/2017.
  */
object consoleDeploy extends App {
  val conf = ConfigFactory.load()


  val historyJson = fromResource("deploymentHistory.json").getLines().mkString("\n")
  val historyOpt = deploymentHistoryParser.parse(historyJson)

  if (historyOpt.isEmpty) {
    println("History not present or corrupted. Exiting.")
    System.exit(-1)
  }
  val history = historyOpt.get

  val scriptDir = conf.getString("fileLocations.deploymentScripts")
  if(!scriptManagement.fileSystemCheck.checkDirectoryConformsToSchema(new File(scriptDir))) {
    println("Script directory does not conform to schema. Exiting")
    System.exit(-1)
  }

  consoleHelper.printHistory(history)

  val env = getEnvironment(history)

  getDeploymentCommand(env)

  print(env)



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

  // TODO: Make all of this bit not shit
  def getDeploymentCommand(env: Environment): DeploymentCommand = {
    consoleHelper.printEnvironmentStatus(env)
    println(s"Latest script version is ${}")
    val question =  """Select an option:
                      |0 - Upgrade to latest version
                      |1 - Rebuild to latest version
                      |2 - Select other version
      """.stripMargin
    val input = consoleHelper.askForValue(question, Seq("0", "1", "2"))

    if(input == "3") {
      println("Option not yet supported. Exiting.")
      System.exit(-1)
    }

    val deploymentType = input match {
      case "0" => DeploymentType.Upgrade
      case "1" => DeploymentType.Rebuild
    }

    DeploymentCommand(env, deploymentType)
  }
}
