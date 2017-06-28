package model

import model.DeploymentType.DeploymentType

/**
  * Created by Matthew.McGowan on 28/06/2017.
  */
case class DeploymentCommand(env: Environment, deployment: DeploymentType)
