Shapeless module
============================

First import the Slick environment and create the data model.

```scala
package net.scalax.ohNoMyCirce.test

import slick.jdbc.H2Profile.api._
import slick.collection.heterogeneous._
import scala.language.existentials //To avoid warnings

object TestSlick {

  case class Model(id: Long, name: String, age: Int, describe: Int, time: Int)

  class ModelTable(cons: Tag) extends Table[Model](cons, "") {

    def id = column[Long]("long")
    def name = column[String]("name")
    def age = column[Int]("age")
    def describe = column[Int]("describe")
    def time = column[Int]("time")

    //The code will be written here later

  }

}
```

Then insert the code to the place of the comment.

```scala
val cols = id :: name :: age :: name :: HNil
override def * = cols.mapTo[Model]
```

Oh, no!
```scala
[error] TestSlick.scala:20:32: type mismatch;
[error]  found   : slick.collection.heterogeneous.HCons[Long,slick.collection.he
terogeneous.HCons[String,slick.collection.heterogeneous.HCons[Int,slick.collecti
on.heterogeneous.HCons[Int,slick.collection.heterogeneous.HCons[Int,slick.collec
tion.heterogeneous.HNil.type]]]]] => net.scalax.ohNoMyCirce.test.TestSlick.Model

[error]  required: Long :: (String :: (Int :: (String :: slick.collection.hetero
geneous.HNil.type))) => net.scalax.ohNoMyCirce.test.TestSlick.Model
[error]     (which expands to)  slick.collection.heterogeneous.HCons[Long,slick.
collection.heterogeneous.HCons[String,slick.collection.heterogeneous.HCons[Int,s
lick.collection.heterogeneous.HCons[String,slick.collection.heterogeneous.HNil.t
ype]]]] => net.scalax.ohNoMyCirce.test.TestSlick.Model
[error]     override def * = cols.mapTo[Model]
[error]                                ^
[error] one error found
```

And then start debugging.
```scala
val cols = id :: name :: age :: name :: HNil
ohNoMyCirce.slickFrom[Model](cols)
override def * = ??? // You should remove mapTo here
```

Error messages:
```scala
[error] E:\pro\workspace\ohNoMyCirce\src\test\scala\net\scalax\ohNoMyCirce\test\
TestSlick.scala:20:33: Generic error:
[error] Case Class Type  : net.scalax.ohNoMyCirce.test.TestSlick.Model
[error] Except HList size: 5
[error] Current size     : 4
[error]     ohNoMyCirce.slickFrom[Model](cols)
[error]                                 ^
[error] one error found
```

Next, add an element to the end of the HList:
```scala
val cols = id :: name :: age :: name :: time :: HNil
ohNoMyCirce.slickFrom[Model](cols)
override def * = ??? // You should remove mapTo here
```

Error messages:
```scala
[error] E:\pro\workspace\ohNoMyCirce\src\test\scala\net\scalax\ohNoMyCirce\test\
TestSlick.scala:20:33: Generic error:
[error] Case Class Type            :
[error] net.scalax.ohNoMyCirce.test.TestSlick.Model
[error] HList size                 : 5
[error] Element index(0 base)      : 3
[error] Expect Property Type       : Int
[error] Property Name              : describe
[error] Not Confirm HList Item Type: String
[error] Code Snippet               :
[error] No confirm code snippet
[error] Error occurred in an application involving default arguments.
[error]     ohNoMyCirce.slickFrom[Model](cols)
[error]                                 ^
[error] one error found
```

Emmm, this error message has been very detailed, but
I can not quickly determine if the wrong location is
`name` or `time`.  
  
Add marks of these code snippets.
```scala
val cols = id :: name :: age :: ohNoMyCirce.slickSnippet(name) :: ohNoMyCirce.slickSnippet(time) :: HNil
ohNoMyCirce.slickFrom[Model](cols)
override def * = ??? // You should remove mapTo here
```

Error messages:
```scala
[error] E:\pro\workspace\ohNoMyCirce\src\test\scala\net\scalax\ohNoMyCirce\test\
TestSlick.scala:20:33: Generic error:
[error] Case Class Type            :
[error] net.scalax.ohNoMyCirce.test.TestSlick.Model
[error] HList size                 : 5
[error] Element index(0 base)      : 3
[error] Expect Property Type       : Int
[error] Property Name              : describe
[error] Not Confirm HList Item Type: String
[error] Code Snippet               :
[error] ModelTable.this.name
[error]     ohNoMyCirce.slickFrom[Model](cols)
[error]                                 ^
[error] one error found
```

Now You can find the error code snippet in the
`Code Snippet` message.  
Fix it:
```scala
val cols = id :: name :: age :: ohNoMyCirce.slickSnippet(describe) :: ohNoMyCirce.slickSnippet(time) :: HNil
ohNoMyCirce.slickFrom[Model](cols)
override def * = ??? // You should remove mapTo here
```

Oh, yes.
```scala
[info] Done compiling.
```

Remove the test code and add the `maoTo` to the code again.  

```scala
val cols = id :: name :: age :: describe :: time :: HNil
override def * = cols.mapTo[Model]
```

Oh, yes.
```scala
[info] Done compiling.
```

Have fun!

The final version of the code above is [here](https://github.com/djx314/ohNoMyCirce/blob/master/src/test/scala/net/scalax/ohNoMyCirce/test/TestSlick.scala).