package net.scalax.ohNoMyShapeless.macros

import scala.reflect.macros.blackbox.Context
import scala.language.higherKinds

object OhNoMyShapelessMacros {

  trait PropertyType[Pro]

  trait ModelGen[Model] {
    def apply[Pro](f: Model => Pro): PropertyType[Pro] = new PropertyType[Pro] {}
  }

  trait ImplicitFetcher1[WrapData] {
    def implicit1(implicit v: WrapData): Unit = {}
  }

  trait ImplicitFetcher2[WrapData] {
    def implicit2(implicit v: WrapData): Unit = {}
  }

  object tag {

    /*trait OhNoTagged[BaseType] { type TagType }
    type Aux[BaseType, T] = BaseType with OhNoTagged[BaseType] { type TagType = T }*/
    trait OhNoTagged[TagType]
    type Aux[BaseType, TagType] = BaseType with OhNoTagged[TagType]

    def apply[U] = new OhNoTagger[U]
    class OhNoTagger[U] {
      def apply[T](t: T): T Aux U = t.asInstanceOf[T Aux U]
    }

    trait `No confirm code snippet`

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

      implicit def implicit1[R, U, T](implicit cv1: T <:< (R Aux U)): TAux[T, R, U] = new TagImplicit {
        type WrapType = T
        type Base = R
        type Snippet = U
      }
    }

    def aa[B, NotConfirm]: TagImplicit.TAux[B, B, NotConfirm] = new TagImplicit { type WrapType = B; type Base = B; type Snippet = NotConfirm }

  }

  class SnippetImpl(val c: scala.reflect.macros.whitebox.Context) {

    import c.universe._

    def impl[T: c.WeakTypeTag](t: c.Expr[T]) = {
      c.Expr(q"""
        trait ${TypeName(t.tree.toString)}
        _root_.net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros.tag[${TypeName(t.tree.toString)}]($t)
      """)
    }

  }

  class OhNoMyShapelessMacrosImpl(val c: Context) {

    import c.universe._

    def impl[Case: c.WeakTypeTag, H: c.WeakTypeTag]: c.Expr[Unit] = {
      val caseType = weakTypeOf[Case]
      val hType = weakTypeOf[H]

      val mg = q"""lazy val mg: _root_.net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros.ModelGen[${caseType.typeSymbol}] = new _root_.net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros.ModelGen[${caseType.typeSymbol}] {}"""

      val members = caseType.members.filter(_.asTerm.isCaseAccessor).map(_.name.toString.trim).toList.reverse.distinct

      def confirmMember(name: String, value: TermName, index: Int) = {
        val freshName1 = c.freshName(name)
        val freshName2 = c.freshName(name)
        val currentTail = (0 to index).tail.foldLeft(q"""$value""": Tree) { (v, _) =>
          q"""$v.tail"""
        }
        q"""
            @_root_.scala.annotation.implicitNotFound(msg = ${Literal(Constant(s"Generic error:\nCase Class Type: $${CaseType}\nExcept HList size: ${members.size}\nCurrent size: ${index.toString}"))})
           trait ${TypeName(freshName1)}[CaseType, HListType]
           object ${TermName(freshName1)} {
             implicit def implicit1[CaseType, HListType, Head, Tail <: _root_.shapeless.HList](implicit cv1: HListType <:< _root_.shapeless.::[Head, Tail])
             : ${TypeName(freshName1)}[CaseType, HListType] = new ${TypeName(freshName1)}[CaseType, HListType] { }
           }
           def ${TermName(freshName2)}[A](a: A) = new _root_.net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros.ImplicitFetcher1[${TypeName(freshName1)}[${caseType}, A]] { }
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
          @_root_.scala.annotation.implicitNotFound(msg = ${Literal(Constant(s"Generic error:\nCase Class Type: $${CaseType}\nHList size: ${members.size}\nElement index: ${index.toString}\nExpect Property Type: $${ProType}\nNot Confirm HList Item Type: $${HListEleType}\nCode Snippet: $${CodeSnippet}"))})
          trait ${TypeName(freshName1)}[CaseType, ProType, HListEleType, CodeSnippet]
          object ${TermName(freshName1)} {
            implicit def implicit1[CaseType, ProType, HListEleType, CodeSnippet](implicit cv1: HListEleType <:< ProType)
            : ${TypeName(freshName1)}[CaseType, ProType, HListEleType, CodeSnippet] = new ${TypeName(freshName1)}[CaseType, ProType, HListEleType, CodeSnippet] { }
          }
          def ${TermName(freshName2)}[A, B, D, E](pro: _root_.net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros.PropertyType[A], item: B)(implicit cv1: _root_.net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros.tag.TagImplicit.TAux[B, D, E] = _root_.net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros.tag.aa[B, ${TypeName(traitName3)}]) = {
            object impl extends _root_.net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros.ImplicitFetcher1[${TypeName(freshName1)}[${caseType}, A, D, E]]
            impl
          }
          ${TermName(freshName2)}(mg(_.${TermName(name)}), ${currentTail}.head).implicit1
         """
      }

      val q = c.Expr[Unit](
        q"""{ (value: ${hType}) =>
              $mg
              ..${members.zipWithIndex.map { case (name, index) => confirmMember(name, TermName("value"), index) }}
              ..${members.zipWithIndex.map { case (name, index) => confirmType(name, TermName("value"), index) }}
            }
           (): _root_.scala.Unit
           """)
      q
    }

  }

}