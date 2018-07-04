package net.scalax.ohNoMyCirce.test

import shapeless._
import scala.language.existentials //To avoid warnings

object TestShapeless {

  case class Model(id: Long, name: String, age: Int, describe: Int, time: Int)

  val model = 36444L :: "sdfsfsd" :: ohNoMyCirce.shapelessSnippet(343) :: ohNoMyCirce.shapelessSnippet {
    def testStr = "I am test string."
    val bb = testStr
    val cc = bb.size
    cc.toLong.toInt
  } :: ohNoMyCirce.shapelessSnippet(486) :: HNil
  ohNoMyCirce.shapelessFrom[Model](model)
  Generic[Model].from(model)

}