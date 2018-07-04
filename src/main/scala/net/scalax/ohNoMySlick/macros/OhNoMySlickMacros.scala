package net.scalax.ohNoMySlick.macros

import slick.lifted._

import scala.reflect.macros.blackbox.Context
import scala.language.higherKinds

object OhNoMySlickMacros {

  trait PropertyType[Pro]

  trait ModelGen[Model] {
    def apply[Pro](f: Model => Pro): PropertyType[Pro] = new PropertyType[Pro] {}
  }

  trait ImplicitFetcher1[WrapData] {
    def implicit1(implicit v: WrapData): Unit = {}
  }

  object tag {

    trait OhNoTagged[BaseType, TagType]
    type Aux[BaseType, TagType] = BaseType with OhNoTagged[BaseType, TagType]

    trait TagApply[Rep] {
      def withImplicit[R, U](implicit shape: Shape[_ <: FlatShapeLevel, Rep, R, U]): MappedProjection[R Aux U, R]
    }

    def apply[U] = new OhNoTagger[U]
    class OhNoTagger[U] {
      def apply[X, R, F](t: X)(implicit shape: Shape[_ <: FlatShapeLevel, X, R, F]): MappedProjection[R Aux U, R] = ShapedValue(t, shape).<>(t => t.asInstanceOf[R Aux U], (_: R Aux U) => Option.empty[R])
    }

    trait TagImplicit {
      type WrapType
      type Base
      type Snippet
    }

    object TagImplicit {
      type TAux[W, B, S] = TagImplicit {
        type WrapType = W
        type Base = B
        type Snippet = S
      }

      implicit def implicit1[R, U]: TAux[R Aux U, R, U] = new TagImplicit {
        type WrapType = R Aux U
        type Base = R
        type Snippet = U
      }

      def defaultImplicit[R, NotConfirm]: TAux[R, R, NotConfirm] = new TagImplicit {
        type WrapType = R
        type Base = R
        type Snippet = NotConfirm
      }

    }

  }

  class SlickSnippetImpl(val c: scala.reflect.macros.whitebox.Context) {

    import c.universe._

    def impl[H: c.WeakTypeTag, Data, O](t: c.Expr[H])(shape: c.Expr[Shape[_ <: FlatShapeLevel, H, Data, O]]) = {
      c.Expr(q"""
        trait ${TypeName(t.tree.toString)}
        _root_.net.scalax.ohNoMySlick.macros.OhNoMySlickMacros.tag[${TypeName(t.tree.toString)}]($t)($shape)
      """)
    }

  }

  class OhNoMySlickMacrosImpl(val c: Context) {

    import c.universe._

    def impl[Case: c.WeakTypeTag, H: c.WeakTypeTag, Rep: c.WeakTypeTag, Out: c.WeakTypeTag](rep: c.Expr[Rep])(shape: c.Expr[slick.lifted.Shape[_ <: slick.lifted.ShapeLevel, Rep, H, Out]]): c.Expr[Unit] = {
      val caseType = weakTypeOf[Case]
      val hType = weakTypeOf[H]

      val mg = q"""lazy val mg: _root_.net.scalax.ohNoMySlick.macros.OhNoMySlickMacros.ModelGen[${caseType.typeSymbol}] = new _root_.net.scalax.ohNoMySlick.macros.OhNoMySlickMacros.ModelGen[${caseType.typeSymbol}] {}"""

      val members = caseType.members.filter(_.asTerm.isCaseAccessor).map(_.name.toString.trim).toList.reverse.distinct

      def confirmMember(name: String, value: TermName, index: Int) = {
        val freshName1 = c.freshName(name)
        val freshName2 = c.freshName(name)
        val currentTail = (0 to index).tail.foldLeft(q"""$value""": Tree) { (v, _) =>
          q"""$v.tail"""
        }
        q"""
            @_root_.scala.annotation.implicitNotFound(msg = ${Literal(Constant(s"Generic error:\nCase Class Type${" " * 2}: $${CaseType}\nExcept HList size: ${members.size}\nCurrent size${" " * 5}: ${index.toString}"))})
           trait ${TypeName(freshName1)}[CaseType, HListType]
           object ${TermName(freshName1)} {
             implicit def implicit1[CaseType, HListType, Head, Tail <: _root_.slick.collection.heterogeneous.HList](implicit cv1: HListType <:< _root_.slick.collection.heterogeneous.HCons[Head, Tail])
             : ${TypeName(freshName1)}[CaseType, HListType] = new ${TypeName(freshName1)}[CaseType, HListType] { }
           }
           def ${TermName(freshName2)}[A](a: A) = new _root_.net.scalax.ohNoMySlick.macros.OhNoMySlickMacros.ImplicitFetcher1[${TypeName(freshName1)}[${caseType}, A]] { }
           ${TermName(freshName2)}(${currentTail}).implicit1
         """
      }

      def confirmType(name: String, value: TermName, index: Int) = {
        val freshName1 = c.freshName(name)
        val freshName2 = c.freshName(name)
        val traitName3 = "No confirm code snippet"
        val currentTail = (0 to index).tail.foldLeft(q"""$value""": Tree) { (v, _) =>
          q"""$v.tail"""
        }
        q"""
          trait ${TypeName(traitName3)}
          @_root_.scala.annotation.implicitNotFound(msg = ${Literal(Constant(s"Generic error:\nCase Class Type${" " * 12}:\n$${CaseType}\nHList size${" " * 17}: ${members.size}\nElement index(0 base)${" " * 6}: ${index.toString}\nExpect Property Type${" " * 7}: $${ProType}\nProperty Name${" " * 14}: ${name}\nNot Confirm HList Item Type: $${HListEleType}\nCode Snippet${" " * 15}:\n$${CodeSnippet}"))})
          trait ${TypeName(freshName1)}[CaseType, ProType, HListEleType, CodeSnippet]
          object ${TermName(freshName1)} {
            implicit def implicit1[CaseType, ProType, HListEleType, CodeSnippet](implicit cv1: HListEleType <:< ProType)
            : ${TypeName(freshName1)}[CaseType, ProType, HListEleType, CodeSnippet] = new ${TypeName(freshName1)}[CaseType, ProType, HListEleType, CodeSnippet] { }
          }
          def ${TermName(freshName2)}[A, B, D, E](pro: _root_.net.scalax.ohNoMySlick.macros.OhNoMySlickMacros.PropertyType[A], item: B)(implicit cv1: _root_.net.scalax.ohNoMySlick.macros.OhNoMySlickMacros.tag.TagImplicit.TAux[B, D, E] = _root_.net.scalax.ohNoMySlick.macros.OhNoMySlickMacros.tag.TagImplicit.defaultImplicit[B, ${TypeName(traitName3)}]) = {
            object impl extends _root_.net.scalax.ohNoMySlick.macros.OhNoMySlickMacros.ImplicitFetcher1[${TypeName(freshName1)}[${caseType}, A, D, E]]
            impl
          }
          ${TermName(freshName2)}(mg(_.${TermName(name)}), ${currentTail}.head).implicit1
         """
      }

      val q = c.Expr[Unit](
        q"""
            { (value: ${hType}) =>
            $mg
            ..${members.zipWithIndex.map { case (name, index) => confirmMember(name, TermName("value"), index) }}
            ..${members.zipWithIndex.map { case (name, index) => confirmType(name, TermName("value"), index) }}
            (): _root_.scala.Unit
          }
          """)
      q
    }

  }

}