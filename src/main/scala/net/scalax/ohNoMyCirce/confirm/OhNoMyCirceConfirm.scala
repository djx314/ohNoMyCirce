package net.scalax.ohNoMyCirce.confirm

import io.circe.Encoder
import shapeless._

import scala.annotation.implicitNotFound

object OhNoMyCirceConfirm {

  def debugFastFail[F[_], Model]: DebugFastFail[F, Model] = new DebugFastFail[F, Model]

  class DebugFastFail[F[_], Model] {
    def count[H <: HList, R <: HList, Target](implicit
      generic: Generic.Aux[Model, H],
      d: DebugEncoder.Aux[F, H, R],
      m: EncoderMapping[F, Model, R, Target]
    ): Target = m.target
  }

  class DebugEncoder[F[_], H <: HList] {
    type Result <: HList
  }
  object DebugEncoder extends DebugEncoderImpl {
    type Aux[F[_], H <: HList, R <: HList] = DebugEncoder[F, H] { type Result = R }
    implicit def implicit1[F[_], M, H <: HList, R <: HList](implicit m: F[M], d: Aux[F, H, R]): Aux[F, M :: H, R] =
      new DebugEncoder[F, M :: H] {
        type Result = R
      }
  }
  trait DebugEncoderImpl {
    implicit def implicit2[F[_], H <: HList]: DebugEncoder.Aux[F, H, H] = new DebugEncoder[F, H] {
      type Result = H
    }
  }

  case class EncoderMapping[F[_], Model, H <: HList, Target](target: Target)
  object EncoderMapping {
    implicit def implicit1[F[_], Model, Head, T <: HList]: EncoderMapping[F, Model, Head :: T, Message1[F, Model, Head]] = EncoderMapping(
      new Message1[F, Model, Head]
    )
    implicit def implicit2[F[_], Model]: EncoderMapping[F, Model, HNil, Message0[F, Model]] = EncoderMapping(new Message0[F, Model])
  }

  class Message0[F[_], T] {
    def message: F[T] = ???
  }

  class Message1[F[_], T, Pro1] {
    def message(implicit encoder: F[Pro1]): F[T] = ???
  }

}
