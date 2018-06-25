import io.circe.{ Decoder, Encoder }
import net.scalax.ohNoMyCirce.macros.OhNoMyCirceMacros

import scala.language.experimental.macros

package object ohNoMyCirce {

  def encoder[T]: Unit = macro OhNoMyCirceMacros.OhNoMyCirceMacrosImpl.impl[T, Encoder]
  def decoder[T]: Unit = macro OhNoMyCirceMacros.OhNoMyCirceMacrosImpl.impl[T, Decoder]

}