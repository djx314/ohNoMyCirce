package net.scalax.ohNoMyCirce.test

import java.util.{ Calendar, Date }

import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._

object TestCirce {

  case class Wrap(id: Long, model: Model, cal: Calendar)
  case class Model(id: Long, name: String, age: Int, describe: String, time: Date)

  val wrap: Wrap = ???

  implicit def i1: Encoder[Calendar] = ???
  implicit def i2: Encoder[Date] = ???
  ohNoMyCirce.encoder[Model] //type parameter changed
  wrap.asJson

  val json: Json = ???

  implicit def i3: Decoder[Calendar] = ???
  implicit def i4: Decoder[Date] = ???
  ohNoMyCirce.decoder[Model]
  json.as[Wrap]

}