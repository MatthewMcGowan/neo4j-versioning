package model

/**
  * Created by Matthew.McGowan on 13/06/2017.
  */
case class Script(majorVersion: Int, minorVersion: Int, patchVersion: Int) {
  val version: String = majorVersion :: minorVersion :: patchVersion :: Nil mkString "."
}
