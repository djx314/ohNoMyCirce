package net.scalax.ohNoMyCirce.test

import java.util.{Calendar, Date}
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import net.scalax.ohNoMyCirce.confirm.OhNoMyCirceConfirm
import net.scalax.ohNoMyCirce.confirm.OhNoMyCirceConfirm.DebugFastFail

object TestCirce {
  def circeEncoder[T]: DebugFastFail[Encoder, T] = OhNoMyCirceConfirm.debugFastFail[Encoder, T]
  def circeDecoder[T]: DebugFastFail[Decoder, T] = OhNoMyCirceConfirm.debugFastFail[Decoder, T]

  case class Wrap(id: Long, cal: Calendar, model: Model)
  case class Model(id: Long, name: String, age: Int, describe: String, time: Date)

  val wrap: Wrap = ???

  implicit def i1: Encoder[Calendar] = ???
  implicit def i2: Encoder[Date]     = ???
  circeEncoder[Wrap].count.message
  circeEncoder[Model].count.message
  wrap.asJson

  val json: Json = ???

  implicit def i3: Decoder[Calendar] = ???
  implicit def i4: Decoder[Date]     = ???
  circeDecoder[Wrap].count.message
  circeDecoder[Model].count.message
  json.as[Wrap]

}
