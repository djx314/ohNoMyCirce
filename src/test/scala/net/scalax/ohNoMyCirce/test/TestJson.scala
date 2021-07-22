package net.scalax.ohNoMyCirce.test

import net.scalax.ohNoMyCirce.confirm.TypeClassDebugger

object TestCirce {

  import java.util.{Calendar, Date}
  import io.circe._
  import io.circe.syntax._
  import io.circe.generic.auto._

  val encoderDebugger: TypeClassDebugger[Encoder] = TypeClassDebugger[Encoder]
  val decoderDebugger: TypeClassDebugger[Decoder] = TypeClassDebugger[Decoder]

  case class Wrap(id: Long, cal: Calendar, model: Model)
  case class Model(id: Long, name: String, age: Int, describe: String, time: Date)

  val wrap: Wrap = ???

  implicit def i1: Encoder[Calendar] = ???
  implicit def i2: Encoder[Date]     = ???
  encoderDebugger.debug[Wrap].count.message
  encoderDebugger.debug[Model].count.message
  wrap.asJson

  val json: Json = ???

  implicit def i3: Decoder[Calendar] = ???
  implicit def i4: Decoder[Date]     = ???
  decoderDebugger.debug[Wrap].count.message
  decoderDebugger.debug[Model].count.message
  json.as[Wrap]

}
