package scriptManagement

import java.io.{File, FileFilter, FilenameFilter}
import java.util.regex.Pattern

/**
  * Created by Matthew.McGowan on 14/06/2017.
  */
object fileSystemCheck {
  private val firstLevelPattern = "^(\\d+\\.)x.x$".r.pattern
  private val secondLevelPattern = "^(\\d+\\.)(\\d+\\.)x$".r.pattern
  private val thirdLevelPattern = "^(\\d+\\.)(\\d+\\.)(\\*|\\d+).cql$".r.pattern

  // TODO: Do something a bit better here. Perhaps return Either[File, String] with error reason for String?
  def checkDirectoryConformsToSchema(f: File): Boolean = {
    val firstLevel = getChildFiles(Seq(f))
    val secondLevel = getChildFiles(firstLevel)
    val thirdLevel = getChildFiles(secondLevel)

    validateFolderLevel(firstLevel, firstLevelPattern) &&
    validateFolderLevel(secondLevel, secondLevelPattern) &&
    validateScriptLevel(thirdLevel, thirdLevelPattern)
  }

  private def validateFolderLevel(fs: Seq[File], pattern: Pattern) =
    fileNamesConform(fs, pattern) && fs.forall(_.isDirectory) && fs.nonEmpty

  private def validateScriptLevel(fs: Seq[File], pattern: Pattern) =
    fileNamesConform(fs, pattern) && fs.forall(_.isFile) && fs.nonEmpty

  // listFiles has no guaranteed return ordering.
  // Here, we guarantee order by name. I.e. "0.0.1" < "0.0.2".
  private def getChildFiles(fs: Seq[File]): Seq[File] =
    fs.filter(_.isDirectory).flatMap(_.listFiles(cqlFilter).sortBy(_.getName))

  private def fileNamesConform(fs: Seq[File], pattern: Pattern): Boolean =
    fs.forall(x => pattern.matcher(x.getName).matches())

  private val cqlFilter = new FileFilter {
    override def accept(pathname: File): Boolean = pathname.getName.endsWith(".cql") || pathname.isDirectory
  }
}
