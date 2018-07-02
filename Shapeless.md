Shapeless module
============================

First import the shapeless environment and create the data model.

```scala
package net.scalax.ohNoMyCirce.test

import shapeless._
import scala.language.existentials //To avoid warnings

object TestShapeless {

  case class Model(id: Long, name: String, age: Int, describe: Int, time: Int)

  //The code will be written here later

}
```

Then insert the code to the place of the comment.

```scala
val model = 36444L :: "sdfsfsd" :: 343 :: {
  def testStr = "I am test string."
  val bb = testStr
  val cc = bb.size
  cc.toLong
} :: HNil
Generic[Model].from(model)
```

Oh, no!
```scala
[error] TestShapeless.scala:16:23: type mismatch;
[error]  found   : Long :: String :: Int :: Long :: shapeless.HNil
[error]  required: Long :: String :: Int :: Int :: Int :: shapeless.HNil
[error]   Generic[Model].from(model)
[error]                       ^
[error] one error found
```

And then start debugging.
```scala
val model = 36444L :: "sdfsfsd" :: 343 :: {
  def testStr = "I am test string."
  val bb = testStr
  val cc = bb.size
  cc.toLong
} :: HNil
ohNoMyCirce.shapelessFrom(model)[Model]
Generic[Model].from(model)
```

Error messages:
```scala
[error] TestShapeless.scala:16:35: Generic error:
[error] Case Class Type  : net.scalax.ohNoMyCirce.test.TestShapeless.Model
[error] Except HList size: 5
[error] Current size     : 4
[error]   ohNoMyCirce.shapelessFrom(model)[Model]
[error]                                   ^
[error] TestShapeless.scala:17:23: type mismatch;
[error]  found   : Long :: String :: Int :: Long :: shapeless.HNil
[error]  required: Long :: String :: Int :: Int :: Int :: shapeless.HNil
[error]   Generic[Model].from(model)
[error]                       ^
[error] two errors found
```

Next, add an element to the end of the HList:
```scala
val model = 36444L :: "sdfsfsd" :: 343 :: {
  def testStr = "I am test string."
  val bb = testStr
  val cc = bb.size
  cc.toLong
} :: 486 :: HNil
ohNoMyCirce.shapelessFrom(model)[Model]
Generic[Model].from(model)
```

Error messages:
```scala
[error] TestShapeless.scala:16:35: Generic error:
[error] Case Class Type            :
[error] net.scalax.ohNoMyCirce.test.TestShapeless.Model
[error] HList size                 : 5
[error] Element index(0 base)      : 3
[error] Expect Property Type       : Int
[error] Property Name              : describe
[error] Not Confirm HList Item Type: Long
[error] Code Snippet               :
[error] No confirm code snippet
[error] Error occurred in an application involving default arguments.
[error]   ohNoMyCirce.shapelessFrom(model)[Model]
[error]                                   ^
[error] TestShapeless.scala:17:23: type mismatch;
[error]  found   : Long :: String :: Int :: Long :: Int :: shapeless.HNil
[error]  required: Long :: String :: Int :: Int :: Int :: shapeless.HNil
[error]   Generic[Model].from(model)
[error]                       ^
[error] two errors found
```

Emmmm, This error message has been very detailed, but
I can not quickly determine if the wrong location is
`343` or `486` or the block.  
Add a mark of a code snippet.
```scala
val model = 36444L :: "sdfsfsd" :: ohNoMyCirce.shapelessSnippet(343) :: ohNoMyCirce.shapelessSnippet {
  def testStr = "I am test string."
  val bb = testStr
  val cc = bb.size
  cc.toLong
} :: ohNoMyCirce.shapelessSnippet(486) :: HNil
ohNoMyCirce.shapelessFrom(model)[Model]
Generic[Model].from(model)
```

Error messages:
```scala
[error] TestShapeless.scala:16:35: Generic error:
[error] Case Class Type            :
[error] net.scalax.ohNoMyCirce.test.TestShapeless.Model
[error] HList size                 : 5
[error] Element index(0 base)      : 3
[error] Expect Property Type       : Int
[error] Property Name              : describe
[error] Not Confirm HList Item Type: Long
[error] Code Snippet               :
[error] {
[error]   def testStr: String = "I am test string.";
[error]   val bb: String = testStr;
[error]   val cc: Int = scala.Predef.augmentString(bb).size;
[error]   cc.toLong
[error] }
[error]   ohNoMyCirce.shapelessFrom(model)[Model]
[error]                                   ^
[error] TestShapeless.scala:17:23: type mismatch;
[error]  found   : Long :: String :: Int with net.scalax.ohNoMyShapeless.macros.
OhNoMyShapelessMacros.tag.OhNoTagged[Int,343] :: Long with net.scalax.ohNoMyShap
eless.macros.OhNoMyShapelessMacros.tag.OhNoTagged[Long,{
[error]   def testStr: String = "I am test string.";
[error]   val bb: String = testStr;
[error]   val cc: Int = scala.Predef.augmentString(bb).size;
[error]   cc.toLong
[error] }] :: Int with net.scalax.ohNoMyShapeless.macros.OhNoMyShapelessMacros.t
ag.OhNoTagged[Int,486] :: shapeless.HNil where type 486 <: AnyRef, type {
[error]   def testStr: String = "I am test string.";
[error]   val bb: String = testStr;
[error]   val cc: Int = scala.Predef.augmentString(bb).size;
[error]   cc.toLong
[error] } <: AnyRef, type 343 <: AnyRef
[error]  required: Long :: String :: Int :: Int :: Int :: shapeless.HNil
[error]   Generic[Model].from(model)
[error]                       ^
[error] two errors found
```

Don't pay attention to second error message, then you can find
the error code snippet is the block.  
Fix it.:
```scala
val model = 36444L :: "sdfsfsd" :: ohNoMyCirce.shapelessSnippet(343) :: ohNoMyCirce.shapelessSnippet {
  def testStr = "I am test string."
  val bb = testStr
  val cc = bb.size
  cc.toLong.toInt
} :: ohNoMyCirce.shapelessSnippet(486) :: HNil
ohNoMyCirce.shapelessFrom(model)[Model]
Generic[Model].from(model)
```

Oh, yes.
```scala
[info] Done compiling.
```

Remove the test code that has just been added and have fun.

The final version of the code above is [here](https://github.com/djx314/ohNoMyCirce/blob/master/src/test/scala/net/scalax/ohNoMyCirce/test/TestShapeless.scala).