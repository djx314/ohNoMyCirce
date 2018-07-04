package net.scalax.ohNoMyCirce.test

import slick.jdbc.H2Profile.api._
import slick.collection.heterogeneous._
import scala.language.existentials //To avoid warnings

object TestSlick {

  case class Model(id: Long, name: String, age: Int, describe: Int, time: Int)

  class ModelTable(cons: Tag) extends Table[Model](cons, "") {

    def id = column[Long]("long")
    def name = column[String]("name")
    def age = column[Int]("age")
    def describe = column[Int]("describe")
    def time = column[Int]("time")

    val cols = id :: name :: age :: describe :: time :: HNil
    ohNoMyCirce.slickFrom[Model](cols)
    override def * = cols.mapTo[Model]

  }

}