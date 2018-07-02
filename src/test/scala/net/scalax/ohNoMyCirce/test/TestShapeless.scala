package net.scalax.ohNoMyCirce.test

import shapeless._
import scala.language.existentials

object TestShapeless {

  case class Model(id: Long, name: String, age: Int, describe: Int, time: Int)

  val aa = 36444L :: "sdfsfsd" :: ohNoMyCirce.snippet(3432) :: ohNoMyCirce.snippet(231) :: ohNoMyCirce.snippet(3) :: HNil
  ohNoMyCirce.shapelessFrom(aa)[Model]
  Generic[Model].from(aa)

}