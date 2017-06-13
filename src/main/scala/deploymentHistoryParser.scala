import model.{Environment, Script}
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import scala.util.Try

/**
  * Created by Matthew.McGowan on 13/06/2017.
  */
object deploymentHistoryParser {

  //implicit val scriptReads: Reads[Environment] =
  //  ((__ \ "scriptsRunSuccessfully").readNullable[Seq[Script]])(Environment.apply _)

  implicit val scriptReads: Reads[Script] = (
      (__ \ "majorVersion").read[Int] and
      (__ \ "minorVersion").read[Int] and
      (__ \ "patchVersion").read[Int]
    )(Script.apply _)

  implicit val environmentReads: Reads[Environment] = (
    (__ \ "environmentName").read[String] and
    (__ \ "scriptsRunSuccessfully").read[Seq[Script]]
  )(Environment.apply _)

  def parse(json: String): Option[Seq[Environment]] = {
    val j: Option[JsValue] = Try(Json.parse(json)).toOption

    // flatMap and lose failure detail. TODO: Log.
    j
      .map(x => x \ "environments")
      .flatMap(_.validate[Seq[Environment]].asOpt)
      // Order by version
      .map(opt => opt.map(
        env => env.copy(env.name, env.history.sortBy(s => (s.majorVersion, s.minorVersion, s.patchVersion)))))
  }
}
