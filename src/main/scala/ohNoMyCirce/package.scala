import io.circe.{ Decoder, Encoder }
import net.scalax.ohNoMyCirce.macros.OhNoMyCirceMacros
import net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros

import scala.language.experimental.macros

package object ohNoMyCirce {

  def encoder[T]: Unit = macro OhNoMyCirceMacros.OhNoMyCirceMacrosImpl.impl[T, Encoder]
  def decoder[T]: Unit = macro OhNoMyCirceMacros.OhNoMyCirceMacrosImpl.impl[T, Decoder]

  trait FromApply[H] {
    def apply[Case]: Unit = macro OhNoMyShapelessMacros.OhNoMyShapelessMacrosImpl.impl[Case, H]
  }

  def shapelessFrom[H](hlist: H): FromApply[H] = new FromApply[H] {}

  def snippet[H, R](t: H): net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros.tag.Aux[H, R] = macro OhNoMyShapelessMacros.SnippetImpl.impl[H]

}