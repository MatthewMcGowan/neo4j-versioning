package model

/**
  * Created by Matthew.McGowan on 28/06/2017.
  */
object DeploymentType extends Enumeration {
  type DeploymentType = Value
  val Upgrade, Rebuild = Value
}
