package net.scalax.ohNoMyCirce.test

import java.util.Calendar
import io.circe._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.{PrintWriter, StringWriter}
import scala.util.Using

case class CirceTestModel(id: Long, name: String, age: Int, describe: String, time: Calendar)
case class CirceTestContent(t: () => Json)

class CirceTestCase extends AnyWordSpec with Matchers {

  "DebugFastFail" should {
    "compile success when each field has encoder" in {
      Using.resource(new StringWriter()) { sw =>
        Using.resource(new PrintWriter(sw)) { pw =>
          Using.resource(GetInterpreter(pw)) { interpreter =>
            val code =
              """
                |import io.circe._
                |import io.circe.syntax._
                |import io.circe.generic.auto._
                |import net.scalax.ohNoMyCirce.test.CirceTestModel
                |import net.scalax.ohNoMyCirce.test.CirceTestContent
                |
                |implicit def calendarEncoder: Encoder[java.util.Calendar] = ???
                |def model: CirceTestModel = ???
                |val result = CirceTestContent(() => model.asJson)
                |""".stripMargin

            interpreter.interpret(code)
            val result = interpreter.valueOfTerm("result")
            result.isEmpty shouldBe false
            result.get.isInstanceOf[CirceTestContent] shouldBe true
            true
          }
        }
      }: Boolean
    }

    "compile failed when some field has no Encoder" in {
      Using.resource(new StringWriter()) { sw =>
        Using.resource(new PrintWriter(sw)) { pw =>
          Using.resource(GetInterpreter(pw)) { interpreter =>
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
              |// implicit def calendarEncoder: Encoder[java.util.Calendar] = ???
              |
              |def debugFastFail[T]: DebugFastFail[Encoder, T] = OhNoMyCirceConfirm.debugFastFail[Encoder, T]
              |debugFastFail[CirceTestModel].count.message
              |
              |def model: CirceTestModel = ???
              |val result = CirceTestContent(() => model.asJson)
              |""".stripMargin

            interpreter.interpret(code)
            val result = interpreter.valueOfTerm("result")
            result.isEmpty shouldBe true
            sw.toString should include("could not find implicit value for parameter encoder: io.circe.Encoder[java.util.Calendar]")
            true
          }
        }
      }: Boolean
    }

    "compile failed when some field has no Decoder" in {
      Using.resource(new StringWriter()) { sw =>
        Using.resource(new PrintWriter(sw)) { pw =>
          Using.resource(GetInterpreter(pw)) { interpreter =>
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

            interpreter.interpret(code)
            sw.toString should include("could not find implicit value for parameter encoder: io.circe.Decoder[java.util.Calendar]")
            true
          }
        }
      }: Boolean
    }

    "works fine with play-json Format" in {
      Using.resource(new StringWriter()) { sw =>
        Using.resource(new PrintWriter(sw)) { pw =>
          Using.resource(GetInterpreter(pw)) { interpreter =>
            val code =
              """
                |import play.api.libs.json._
                |import play.api.libs.functional.syntax._
                |import net.scalax.ohNoMyCirce.test.CirceTestModel
                |import net.scalax.ohNoMyCirce.test.CirceTestContent
                |import net.scalax.ohNoMyCirce.confirm.OhNoMyCirceConfirm
                |import net.scalax.ohNoMyCirce.confirm.OhNoMyCirceConfirm.DebugFastFail
                |
                |def debugFastFail[T]: DebugFastFail[Format, T] = OhNoMyCirceConfirm.debugFastFail[Format, T]
                |debugFastFail[CirceTestModel].count.message
                |""".stripMargin

            interpreter.interpret(code)
            sw.toString should include(
              "No Json formatter found for type java.util.Calendar. Try to implement an implicit Format for this type."
            )
            true
          }
        }
      }: Boolean
    }

  }

}
