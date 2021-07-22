package net.scalax.ohNoMyCirce.confirm

import shapeless._

class TypeClassDebugger[F[_]] {

  def debug[Model]: DebugFastFail[Model] = new DebugFastFail[Model]

  class DebugFastFail[Model] {
    def count[H <: HList, R <: HList, Target](implicit
      generic: Generic.Aux[Model, H],
      d: DebugEncoder.Aux[H, R],
      m: EncoderMapping[Model, R, Target]
    ): Target = m.target
  }

  class DebugEncoder[H <: HList] {
    type Result <: HList
  }
  object DebugEncoder extends DebugEncoderImpl {
    type Aux[H <: HList, R <: HList] = DebugEncoder[H] { type Result = R }
    implicit def implicit1[M, H <: HList, R <: HList](implicit m: F[M], d: Aux[H, R]): Aux[M :: H, R] =
      new DebugEncoder[M :: H] {
        type Result = R
      }
  }
  trait DebugEncoderImpl {
    implicit def implicit2[H <: HList]: DebugEncoder.Aux[H, H] = new DebugEncoder[H] {
      type Result = H
    }
  }

  case class EncoderMapping[Model, H <: HList, Target](target: Target)
  object EncoderMapping {
    implicit def implicit1[Model, Head, T <: HList]: EncoderMapping[Model, Head :: T, Message1[Model, Head]] = EncoderMapping(
      new Message1[Model, Head]
    )
    implicit def implicit2[Model]: EncoderMapping[Model, HNil, Message0[Model]] = EncoderMapping(new Message0[Model])
  }

  class Message0[T] {
    def message: F[T] = ???
  }

  class Message1[T, Pro1] {
    def message(implicit encoder: F[Pro1]): F[T] = ???
  }

}

object TypeClassDebugger {
  def apply[F[_]]: TypeClassDebugger[F] = new TypeClassDebugger[F]
}
