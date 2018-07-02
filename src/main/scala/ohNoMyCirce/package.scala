import io.circe.{ Decoder, Encoder }
import net.scalax.ohNoMyCirce.macros.OhNoMyCirceMacros
import net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros

import scala.language.experimental.macros

package object ohNoMyCirce {

  def circeEncoder[T]: Unit = macro OhNoMyCirceMacros.OhNoMyCirceMacrosImpl.impl[T, Encoder]
  def circeDecoder[T]: Unit = macro OhNoMyCirceMacros.OhNoMyCirceMacrosImpl.impl[T, Decoder]

  trait ShapelessFromApply[H] {
    def apply[Case]: Unit = macro OhNoMyShapelessMacros.OhNoMyShapelessMacrosImpl.impl[Case, H]
  }

  def shapelessFrom[H](hlist: H): ShapelessFromApply[H] = new ShapelessFromApply[H] {}

  def shapelessSnippet[H, R](t: H): net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros.tag.Aux[H, R] = macro OhNoMyShapelessMacros.ShapelessSnippetImpl.impl[H]

}