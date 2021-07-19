import io.circe.{Decoder, Encoder}
import net.scalax.ohNoMyCirce.confirm.OhNoMyCirceConfirm
import net.scalax.ohNoMyCirce.confirm.OhNoMyCirceConfirm.DebugFastFail
import net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros
import net.scalax.ohNoMySlick.macros.OhNoMySlickMacros
import slick.lifted.{FlatShapeLevel, MappedProjection, Shape, ShapeLevel}

import scala.language.experimental.macros

package object ohNoMyCirce {

  def circeEncoder[T]: DebugFastFail[Encoder, T] = OhNoMyCirceConfirm.debugFastFail[Encoder, T]
  def circeDecoder[T]: DebugFastFail[Decoder, T] = OhNoMyCirceConfirm.debugFastFail[Decoder, T]

  trait ShapelessFromApply[Case] {
    def apply[H](hlist: H): Unit = macro OhNoMyShapelessMacros.OhNoMyShapelessMacrosImpl.impl[Case, H]
  }

  def shapelessFrom[Case]: ShapelessFromApply[Case] = new ShapelessFromApply[Case] {}

  def shapelessSnippet[H, R](t: H): net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros.tag.Aux[H, R] =
    macro OhNoMyShapelessMacros.ShapelessSnippetImpl.impl[H]

  trait SlickFromApply[Case] {
    def apply[H, Data, E](rep: H)(implicit shape: Shape[_ <: ShapeLevel, H, Data, E]): Unit =
      macro OhNoMySlickMacros.OhNoMySlickMacrosImpl.impl[Case, Data, H, E]
  }

  def slickFrom[Case]: SlickFromApply[Case] = new SlickFromApply[Case] {}

  def slickSnippet[H, R, Data, O](t: H)(implicit
    shape: Shape[_ <: FlatShapeLevel, H, Data, O]
  ): MappedProjection[net.scalax.ohNoMySlick.macros.OhNoMySlickMacros.tag.Aux[Data, R], Data] =
    macro OhNoMySlickMacros.SlickSnippetImpl.impl[H, Data, O]

}
