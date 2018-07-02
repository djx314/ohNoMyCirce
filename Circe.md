Circe module
============================

First import the Circe environment and create the data model.

```scala
package net.scalax.ohNoMyCirce.test

import java.util.{ Calendar, Date }

import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._

object TestCirce {

  case class Wrap(id: Long, model: Model, cal: Calendar)
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
[error] TestCirce.scala:19:8: could not find implicit value for parameter encoder:
io.circe.Encoder[net.scalax.ohNoMyCirce.test.Test.Wrap]
[error]   wrap.asJson
[error]        ^
[error] one error found
```

And then start debugging.
```scala
val wrap: Wrap = ???
ohNoMyCirce.circeEncoder[Wrap]
wrap.asJson
```

Error messages:
```scala
[error] TestCirce.scala:18:22: Can not find implicit value for Circe.
[error] Case Class Name: net.scalax.ohNoMyCirce.test.Test.Wrap
[error] Property Type: java.util.Calendar
[error] Property Name: cal
[error] Implicit Type: io.circe.Encoder[java.util.Calendar]
[error]   ohNoMyCirce.circeEncoder[Wrap]
[error]                      ^
[error] TestCirce.scala:19:8: could not find implicit value for parameter encoder:
io.circe.Encoder[net.scalax.ohNoMyCirce.test.Test.Wrap]
[error]   wrap.asJson
[error]        ^
[error] two errors found
```

Next:
```scala
val wrap: Wrap = ???
implicit def i1: Encoder[Calendar] = ???
ohNoMyCirce.circeEncoder[Wrap]
wrap.asJson
```

Error messages:
```scala
[error] TestCirce.scala:18:22: Can not find implicit value for Circe.
[error] Case Class Name: net.scalax.ohNoMyCirce.test.Test.Wrap
[error] Property Type: net.scalax.ohNoMyCirce.test.Test.Model
[error] Property Name: model
[error] Implicit Type: io.circe.Encoder[net.scalax.ohNoMyCirce.test.Test.Model]
[error]   ohNoMyCirce.circeEncoder[Wrap]
[error]                      ^
[error] TestCirce.scala:19:8: could not find implicit value for parameter encoder:
io.circe.Encoder[net.scalax.ohNoMyCirce.test.Test.Wrap]
[error]   wrap.asJson
[error]        ^
[error] two errors found
```

Next:
```scala
val wrap: Wrap = ???
implicit def i1: Encoder[Calendar] = ???
ohNoMyCirce.circeEncoder[Model] //type parameter changed
wrap.asJson
```

Error messages:
```scala
[error] TestCirce.scala:18:22: Can not find implicit value for Circe.
[error] Case Class Name: net.scalax.ohNoMyCirce.test.Test.Model
[error] Property Type: java.util.Date
[error] Property Name: time
[error] Implicit Type: io.circe.Encoder[java.util.Date]
[error]   ohNoMyCirce.circeEncoder[Model] //type parameter changed
[error]                      ^
[error] TestCirce.scala:19:8: could not find implicit value for parameter encoder:
io.circe.Encoder[net.scalax.ohNoMyCirce.test.Test.Wrap]
[error]   wrap.asJson
[error]        ^
[error] two errors found
```

Next:
```scala
val wrap: Wrap = ???
implicit def i1: Encoder[Calendar] = ???
implicit def i2: Encoder[Date] = ???
ohNoMyCirce.circeEncoder[Model] //type parameter changed
wrap.asJson
```

Oh, yes.
```scala
[info] Done compiling.
```

By changing `ohNoMyCirce.circeEncoder` to `ohNoMyCirce.decoder`,
you can debug `io.circe.Decoder` similarly.

The final version of the code above is [here](https://github.com/djx314/ohNoMyCirce/blob/master/src/test/scala/net/scalax/ohNoMyCirce/test/TestCirce.scala).

These `ohNoMyCirce` macro only generate many type definitions. At runtime, it does nothing except return a `Unit` result. But it's best to remove it after finishing debugging.