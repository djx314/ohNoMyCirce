package net.scalax.ohNoMyCirce.test

import java.util.Date
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import net.scalax.ohNoMyCirce.confirm.OhNoMyCirceConfirm
import net.scalax.ohNoMyCirce.confirm.OhNoMyCirceConfirm.DebugFastFail

import java.io.{PrintWriter, StringWriter}
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.IMain
import scala.tools.nsc.interpreter.shell.ReplReporterImpl
import scala.util.Using

case class CirceTestModel(id: Long, name: String, age: Int, describe: String, time: Date)
case class CirceTestContent(t: () => Json)

object TestCirce1 {

  def main(arr: Array[String]): Unit = {
    val settings = new Settings
    settings.usejavacp.value = true
    val stringWriter = new StringWriter()
    Using.resources(stringWriter, new PrintWriter(stringWriter)) { (sw, pw) =>
      val eval = new IMain(settings, new ReplReporterImpl(settings, pw))
      val code =
        """
            |import io.circe._
            |import io.circe.syntax._
            |import io.circe.generic.auto._
            |import net.scalax.ohNoMyCirce.test.CirceTestModel
            |import net.scalax.ohNoMyCirce.test.CirceTestContent
            |
            |// implicit def dateEncoder: Encoder[java.util.Date] = ???
            |def model: CirceTestModel = ???
            |val result = CirceTestContent(() => model.asJson)
            |""".stripMargin

      eval.interpret(code)
      // val model = eval.valueOfTerm("result").asInstanceOf[CirceTestContent]
      println(sw.toString)
      println(sw.toString)
      println(sw.toString)
      println(sw.toString)
      println(sw.toString)
    }

  }

}
