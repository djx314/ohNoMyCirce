package net.scalax.ohNoMyCirce.test

import scala.io.StdIn

object Testaa extends App {

  val aa = List(1, 2, 3, 4, 5)

  ((Iterator continually StdIn.readLine()).zipWithIndex takeWhile {
    case (input, index) => ((index < (5 - 1)) && !aa.contains(input.toInt))
  }).toList

}
