import com.github.nadavwr.pthread._

object PrettySample extends App {

  // javalib's ThreadLocal are just stubs, but println seems
  // to work fine so long as no races occur during initialization
  println("main: println() should first be used on main thread")

  val thread1 =
    PThread.start {
      println("thread1: entering (sleeping 1000ms)")
      Thread.sleep(1000)
      println("thread1: departing")
    }

  Thread.sleep(200)

  val thread2 =
    PThread.start {
      println("thread2: entering (sleeping 500ms)")
      Thread.sleep(500)
      println("thread2: departing")
    }

  thread1.join()
  println("main: thread1 joined")

  thread2.join()
  println("main: thread2 joined")
}
