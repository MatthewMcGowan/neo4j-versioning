package deploymentHistory

import model.{Environment, Script}
import org.scalatest.FunSpec

/**
  * Created by Matthew.McGowan on 13/06/2017.
  */
class deploymentHistoryParserSpec extends FunSpec {

  describe("A deployment history parser") {
    describe("given valid JSON") {
      describe("with an environment present") {
        describe("and a script history") {
          it("should return the environment with all scripts") {
            val result = deploymentHistoryParser.parse(fullValidJson)

            val scripts = result.get.head.history // This should exist. Otherwise test fail with exception, fine.

            assert(scripts.contains(Script(0, 0, 1)))
            assert(scripts.contains(Script(0, 1, 0)))
          }

          it("should return all scripts in version order") {
            val result = deploymentHistoryParser.parse(fullValidJson)

            val scripts = result.get.head.history // This should exist. Otherwise test fail with exception, fine.

            assert(scripts == Seq(Script(0, 0, 1), Script(0, 1, 0), Script(0, 1, 1)))
          }
        }

        describe("and no script history") {
          it("should return the Environment with an empty sequence of Scripts") {
            val j =
              """{
                |  "environments": [
                |    {
                |      "environmentName": "afltradev",
                |      "environmentAddress" : "4 Privet Drive",
                |      "scriptsRunSuccessfully": []
                |    }
                |  ]
                |}""".stripMargin

            val result = deploymentHistoryParser.parse(j)

            assert(result.contains(Seq(Environment("afltradev", "4 Privet Drive", Seq()))))
          }
        }
      }

      describe("with no environment present") {
        it("should return an empty sequence of Environments") {
          val j = "{ \"environments\" : []}"

          val result = deploymentHistoryParser.parse(j)

          assert(result.contains(Seq()))
        }
      }

      describe("without an 'environments' section") {
        it("should return None") {
          val j = "\"a\" : \"test\""

          val result = deploymentHistoryParser.parse(j)

          assert(result.isEmpty)
        }
      }
    }

    describe("given invalid JSON") {
      it("should return None") {
        val j = "test"

        val result = deploymentHistoryParser.parse(j)

        assert(result.isEmpty)
      }
    }
  }

  // The below JSON probably naturally exist, but we allow it.
  // It's history is out of order.
  private val fullValidJson = """{
                                |  "environments": [
                                |    {
                                |      "environmentName": "afltradev",
                                |      "environmentAddress" : "4 Privet Drive",
                                |      "scriptsRunSuccessfully": [
                                |        {
                                |          "majorVersion": 0,
                                |          "minorVersion": 0,
                                |          "patchVersion": 1
                                |        },
                                |        {
                                |          "majorVersion": 0,
                                |          "minorVersion": 1,
                                |          "patchVersion": 1
                                |        },
                                |        {
                                |          "majorVersion": 0,
                                |          "minorVersion": 1,
                                |          "patchVersion": 0
                                |        }
                                |      ]
                                |    }
                                |  ]
                                |}""".stripMargin
}