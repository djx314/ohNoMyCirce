package net.scalax.ohNoMyCirce.test

import java.util.Date
import io.circe._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.{PrintWriter, StringWriter}
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.IMain
import scala.tools.nsc.interpreter.shell.ReplReporterImpl
import scala.util.Using

case class CirceTestModel(id: Long, name: String, age: Int, describe: String, time: Date)
case class CirceTestContent(t: () => Json)

class CirceTestCase extends AnyWordSpec with Matchers {
  val settings = new Settings
  settings.usejavacp.value = true

  "DebugFastFail" should {
    "compile success when each field has encoder" in {
      Using.resource(new StringWriter()) { sw =>
        Using.resource(new PrintWriter(sw)) { pw =>
          Using.resource(new IMain(settings, new ReplReporterImpl(settings, pw))) { eval =>
            val code =
              """
                |import io.circe._
                |import io.circe.syntax._
                |import io.circe.generic.auto._
                |import net.scalax.ohNoMyCirce.test.CirceTestModel
                |import net.scalax.ohNoMyCirce.test.CirceTestContent
                |
                |implicit def dateEncoder: Encoder[java.util.Date] = ???
                |def model: CirceTestModel = ???
                |val result = CirceTestContent(() => model.asJson)
                |""".stripMargin

            eval.interpret(code)
            val result = eval.valueOfTerm("result")
            result.isEmpty shouldBe false
            result.get.isInstanceOf[CirceTestContent] shouldBe true
          }
        }
      }
    }

    "compile failed when some field has no Encoder" in {
      Using.resource(new StringWriter()) { sw =>
        Using.resource(new PrintWriter(sw)) { pw =>
          Using.resource(new IMain(settings, new ReplReporterImpl(settings, pw))) { eval =>
            val code =
              """
              |import io.circe._
              |import io.circe.syntax._
              |import io.circe.generic.auto._
              |import net.scalax.ohNoMyCirce.test.CirceTestModel
              |import net.scalax.ohNoMyCirce.test.CirceTestContent
              |import net.scalax.ohNoMyCirce.confirm.OhNoMyCirceConfirm
              |import net.scalax.ohNoMyCirce.confirm.OhNoMyCirceConfirm.DebugFastFail
              |
              |// implicit def dateEncoder: Encoder[java.util.Date] = ???
              |
              |def debugFastFail[T]: DebugFastFail[Encoder, T] = OhNoMyCirceConfirm.debugFastFail[Encoder, T]
              |debugFastFail[CirceTestModel].count.message
              |
              |def model: CirceTestModel = ???
              |val result = CirceTestContent(() => model.asJson)
              |""".stripMargin

            eval.interpret(code)
            val result = eval.valueOfTerm("result")
            result.isEmpty shouldBe true
            sw.toString should include("could not find implicit value for parameter encoder: io.circe.Encoder[java.util.Date]")
          }
        }
      }
    }

    "compile failed when some field has no Decoder" in {
      Using.resource(new StringWriter()) { sw =>
        Using.resource(new PrintWriter(sw)) { pw =>
          Using.resource(new IMain(settings, new ReplReporterImpl(settings, pw))) { eval =>
            val code =
              """
                |import io.circe._
                |import io.circe.syntax._
                |import io.circe.generic.auto._
                |import net.scalax.ohNoMyCirce.test.CirceTestModel
                |import net.scalax.ohNoMyCirce.test.CirceTestContent
                |import net.scalax.ohNoMyCirce.confirm.OhNoMyCirceConfirm
                |import net.scalax.ohNoMyCirce.confirm.OhNoMyCirceConfirm.DebugFastFail
                |
                |def debugFastFail[T]: DebugFastFail[Decoder, T] = OhNoMyCirceConfirm.debugFastFail[Decoder, T]
                |debugFastFail[CirceTestModel].count.message
                |""".stripMargin

            eval.interpret(code)
            sw.toString should include("could not find implicit value for parameter encoder: io.circe.Decoder[java.util.Date]")
          }
        }
      }
    }

  }

}
