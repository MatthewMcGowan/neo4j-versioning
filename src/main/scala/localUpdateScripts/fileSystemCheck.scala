package localUpdateScripts

import java.io.File
import java.util.regex.Pattern

/**
  * Created by Matthew.McGowan on 14/06/2017.
  */
object fileSystemCheck {
  private val firstLevelPattern = "^(\\d+\\.)x.x$".r.pattern
  private val secondLevelPattern = "^(\\d+\\.)(\\d+\\.)x$".r.pattern
  private val thirdLevelPattern = "^(\\d+\\.)(\\d+\\.)(\\*|\\d+).cql$".r.pattern

  def checkDirectoryConformsToSchema(f: File): Boolean = {
    def getChildFiles(fs: Seq[File]): Seq[File] = fs.flatMap(listChildren)

    def filesNamesConform(fs: Seq[File], pattern: Pattern) =
      fs.forall(x => pattern.matcher(x.getName).matches())

    val firstLevel = getChildFiles(Seq(f))
    val secondLevel = getChildFiles(firstLevel)
    val thirdLevel = getChildFiles(secondLevel)

    filesNamesConform(firstLevel, firstLevelPattern) &&
    filesNamesConform(secondLevel, secondLevelPattern) &&
    filesNamesConform(thirdLevel, thirdLevelPattern)
  }

  // listFiles has no guaranteed return ordering.
  // Here, we guarantee order by name. I.e. "0.0.1" < "0.0.2".
  def listChildren(f: File): Seq[File] = f.listFiles().sortBy(_.getName)

  def getFileTree(f: File): Stream[File] =
    f #:: (if (f.isDirectory) listChildren(f).toStream.flatMap(getFileTree)
    else Stream.empty)
}
