package net.scalax.ohNoMyCirce.test

import java.io.PrintWriter
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.IMain
import scala.tools.nsc.interpreter.shell.ReplReporterImpl

object GetIMain {
  def apply(printlnWriter: PrintWriter): IMain = {
    val settings = new Settings
    settings.usejavacp.value = true
    new IMain(settings, new ReplReporterImpl(settings, printlnWriter))
  }
}
