package scriptManagement

import java.io.File

import org.scalatest.FunSpec

import scala.io.Source

/**
  * Created by Matt on 16/06/2017.
  */
class fileSystemCheckSpec extends FunSpec {
  val relDir = "src/test/resources/testDirectories/"

  describe("A file system checker") {
    describe("given a valid directory structure") {
      describe("with a .cql file") {
        describe("at the first level") {
          it("should return false") {
            val f = new File(relDir + "extraFileAtFirstLevel")

            assert(!fileSystemCheck.checkDirectoryConformsToSchema(f))
          }
        }

        describe("at the second level") {
          it("should return false") {
            val f = new File(relDir + "extraFileAtSecondLevel")

            assert(!fileSystemCheck.checkDirectoryConformsToSchema(f))
          }
        }

        describe("at the third level level") {
          describe("which is correctly named") {
            it("should return true") {
              val f = new File(relDir + "validDirectory")

              assert(fileSystemCheck.checkDirectoryConformsToSchema(f))
            }
          }

          describe("which is incorrectly named") {
            it("should return false") {
              val f = new File(relDir + "extraFileAtFirstLevel")

              assert(!fileSystemCheck.checkDirectoryConformsToSchema(f))
            }
          }
        }
      }

      describe("with multiple .cql files") {
        it("should have all files grouped in the correct directory") {
          ???
        }
      }
    }
    describe("given an invalid directory structure") {
      describe("missing the first level") {
        it("should return false") {
          val f = new File(relDir + "missingDirectoriesAtFirstLevel")

          assert(!fileSystemCheck.checkDirectoryConformsToSchema(f))
        }
      }

      describe("missing the second level") {
        it("should return false") {
          val f = new File(relDir + "missingDirectoriesAtSecondLevel")

          assert(!fileSystemCheck.checkDirectoryConformsToSchema(f))
        }
      }
    }

  }
}