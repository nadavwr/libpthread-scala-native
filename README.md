This repository contains a barebones proof-of-concept for pthread-based
threads API for Scala Native.

## Getting It

In a Scala Native project, add a resolver to the repository where
this library is published, and add a `%%%` dependency:

```scala
resolvers += Resolver.bintrayRepo("nadavwr", "maven"),
libraryDependencies += "com.github.nadavwr" %%% "libpthread-scala-native" % "0.1.0"
```


## Using It

If you clone the repository, take a peek at `prettySample`.

Here's the gist of it:

```scala
import com.github.nadavwr.pthread._

// println() relies on javalib's ThreadLocal, which are just stubs.
// Seems to work fine so long as no races occur during initialization.
println("main: println() should first be used on main thread")

val thread1 =
  PThread.start {
    println("thread1: entering (sleeping 1000ms)")
    Thread.sleep(1000)
    println("thread1: departing")
  }

thread1.join()
println("main: thread1 joined")
```
