package net.scalax.ohNoMyCirce.macros

import scala.reflect.macros.blackbox.Context
import scala.language.higherKinds

object OhNoMyCirceMacros {

  trait PropertyType[Pro]

  trait ModelGen[Model] {
    def apply[Pro](f: Model => Pro): PropertyType[Pro] = new PropertyType[Pro] {}
  }

  trait CirceImplicit[WrapData] {
    def confirm(implicit v: WrapData): WrapData = v
  }

  class OhNoMyCirceMacrosImpl(val c: Context) {

    import c.universe._

    def impl[Case: c.WeakTypeTag, R[_]](implicit rImplicit: c.WeakTypeTag[R[_]]): c.Expr[Unit] = {
      val weakType = weakTypeOf[Case]
      val mg = q"""def mg: _root_.net.scalax.ohNoMyCirce.macros.OhNoMyCirceMacros.ModelGen[${weakType.typeSymbol}] = new _root_.net.scalax.ohNoMyCirce.macros.OhNoMyCirceMacros.ModelGen[${weakType.typeSymbol}] {}"""
      def propertyConfirm(proName: String) = {
        val helperTrait1 = c.freshName(proName)
        val helperDef2 = c.freshName(proName)
        q"""
          @_root_.scala.annotation.implicitNotFound(msg = ${Literal(Constant(s"Can not find implicit value for Circe.\nCase Class Name: $${Model}\nProperty Type: $${Pro}\nProperty Name: ${proName}\nImplicit Type: $${WrapPro}"))})
          trait ${TypeName(helperTrait1)}[Model, Pro, WrapPro]

          object ${TermName(helperTrait1)} {
            implicit def ${TermName(c.freshName(proName))}[Model, Pro, WrapPro](implicit i: WrapPro): ${TypeName(helperTrait1)}[Model, Pro, WrapPro] = new ${TypeName(helperTrait1)}[Model, Pro, WrapPro] {}
          }
          def ${TermName(helperDef2)}[S, T](p: _root_.net.scalax.ohNoMyCirce.macros.OhNoMyCirceMacros.PropertyType[T]): _root_.net.scalax.ohNoMyCirce.macros.OhNoMyCirceMacros.CirceImplicit[${TypeName(helperTrait1)}[${weakTypeOf[Case].typeSymbol}, T, ${weakTypeOf[R[_]].typeSymbol}[T]]] =
            new  _root_.net.scalax.ohNoMyCirce.macros.OhNoMyCirceMacros.CirceImplicit[${TypeName(helperTrait1)}[${weakTypeOf[Case].typeSymbol}, T, ${weakTypeOf[R[_]].typeSymbol}[T]]] {}

          def ${TermName(c.freshName(proName))} = ${TermName(helperDef2)}(mg(s => s.${TermName(proName)})).confirm
         """
      }

      val q = c.Expr[Unit](
        q"""{
            ${mg}
           ..${weakType.members.filter(_.asTerm.isCaseAccessor).map(s => propertyConfirm(s.name.toString.trim))}
           (): _root_.scala.Unit
           }""")
      q
    }

  }

}