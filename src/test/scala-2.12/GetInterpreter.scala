package net.scalax.ohNoMyCirce.test

import java.io.PrintWriter
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.IMain

object GetInterpreter {
  def apply(printlnWriter: PrintWriter): IMain = {
    val settings = new Settings
    settings.usejavacp.value = true
    new IMain(settings, printlnWriter)
  }
}
