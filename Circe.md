Circe module
============================

First import the Circe environment and copy the debug
code([link](./src/main/scala/net/scalax/ohNoMyCirce/confirm/OhNoMyCirceConfirm.scala))
to your project.

```scala
package net.scalax.ohNoMyCirce.test

import java.util.{ Calendar, Date }

import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._

object TestCirce {

  case class Wrap(id: Long, cal: Calendar, model: Model)
  case class Model(id: Long, name: String, age: Int, describe: String, time: Date)

  //The code will be written here later

}
```

Then insert the code to the place of the comment.

```scala
val wrap: Wrap = ???
wrap.asJson
```

Oh, no!
```scala
[info] compiling 1 Scala source to E:\pro\workspace\ohNoMyCirce\target\scala-2.13\test-classes ...
[error] E:\pro\workspace\ohNoMyCirce\src\test\scala\net\scalax\ohNoMyCirce\test\TestCirce.scala:23:8: could not find implicit value for parameter encoder: io.circe.Encoder[net.scalax.ohNoMyCirce.test.TestCirce.Wrap]
[error]   wrap.asJson
[error]        ^
[error] one error found
[error] (Test / compileIncremental) Compilation failed
```

And then start debugging.
```scala
val wrap: Wrap = ???

val encoderDebugger: TypeClassDebugger[Encoder] = TypeClassDebugger[Encoder]
encoderDebugger.debug[Wrap].count.message

wrap.asJson
```

Error messages:
```scala
[error] E:\pro\workspace\ohNoMyCirce\src\test\scala\net\scalax\ohNoMyCirce\test\TestJson.scala:22:37: could not find implicit value for parameter encoder: io.circe.Encoder[java.util.Calendar]
[error]   encoderDebugger.debug[Wrap].count.message
[error]                                     ^
[error] E:\pro\workspace\ohNoMyCirce\src\test\scala\net\scalax\ohNoMyCirce\test\TestJson.scala:24:8: could not find implicit value for parameter encoder: io.circe.Encoder[net.scalax.ohNoMyCirce.test.TestCirce.Wrap]
[error]   wrap.asJson
[error]        ^
[error] two errors found
[error] (Test / compileIncremental) Compilation failed
```

Next:
```scala
val wrap: Wrap = ???
implicit def i1: Encoder[Calendar] = ???

val encoderDebugger: TypeClassDebugger[Encoder] = TypeClassDebugger[Encoder]
encoderDebugger.debug[Wrap].count.message

wrap.asJson
```

Error messages:
```scala
[error] E:\pro\workspace\ohNoMyCirce\src\test\scala\net\scalax\ohNoMyCirce\test\TestJson.scala:22:37: could not find implicit value for parameter encoder: io.circe.Encoder[net.scalax.ohNoMyCirce.test.TestCirce.Model]
[error]   encoderDebugger.debug[Wrap].count.message
[error]                                     ^
[error] E:\pro\workspace\ohNoMyCirce\src\test\scala\net\scalax\ohNoMyCirce\test\TestJson.scala:24:8: could not find implicit value for parameter encoder: io.circe.Encoder[net.scalax.ohNoMyCirce.test.TestCirce.Wrap]
[error]   wrap.asJson
[error]        ^
[error] two errors found
[error] (Test / compileIncremental) Compilation failed
```

Next:
```scala
val wrap: Wrap = ???
implicit def i1: Encoder[Calendar] = ???

val encoderDebugger: TypeClassDebugger[Encoder] = TypeClassDebugger[Encoder]
encoderDebugger.debug[Model].count.message //type parameter changed

wrap.asJson
```

Error messages:
```scala
[error] E:\pro\workspace\ohNoMyCirce\src\test\scala\net\scalax\ohNoMyCirce\test\TestJson.scala:23:38: could not find implicit value for parameter encoder: io.circe.Encoder[java.util.Date]
[error]   encoderDebugger.debug[Model].count.message
[error]                                      ^
[error] E:\pro\workspace\ohNoMyCirce\src\test\scala\net\scalax\ohNoMyCirce\test\TestJson.scala:24:8: could not find implicit value for parameter encoder: io.circe.Encoder[net.scalax.ohNoMyCirce.test.TestCirce.Wrap]
[error]   wrap.asJson
[error]        ^
[error] two errors found
[error] (Test / compileIncremental) Compilation failed
```

Next:
```scala
val wrap: Wrap = ???
implicit def i1: Encoder[Calendar] = ???
implicit def i2: Encoder[Date] = ???

val encoderDebugger: TypeClassDebugger[Encoder] = TypeClassDebugger[Encoder]
encoderDebugger.debug[Model].count.message //type parameter changed

wrap.asJson
```

Oh, yes.
```scala
[info] Done compiling.
```

Remove the debug code.

By changing
```scala
val encoderDebugger: TypeClassDebugger[Encoder] = TypeClassDebugger[Encoder]
```
to
```scala
val decoderDebugger: TypeClassDebugger[Decoder] = TypeClassDebugger[Decoder]
```
you can debug `io.circe.Decoder` similarly.

Yes, it can debug `play-json` and many other type class based case class generic.

The final version of the code above is [here](./src/test/scala/net/scalax/ohNoMyCirce/test/TestJson.scala).