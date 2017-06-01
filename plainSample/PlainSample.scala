import scala.scalanative.native._
import com.github.nadavwr.pthread._

object PlainSample extends App {

  // javalib's ThreadLocal are just stubs, but println seems
  // to work fine so long as no races occur during initialization
  println("main: println() should first be used on main thread")

  val f: CFunctionPtr1[Ptr[Byte], Ptr[Byte]] =
    (p: Ptr[Byte]) => {
      println("thread1: entering (sleeping 1000ms)")
      Thread.sleep(1000)
      println("thread1: departing")
      null
    }

  val thread1 = stackalloc[pthread_t]
  pthread_create(thread1, null, f, null)
  println("main: thread1 started")

  pthread_join(!thread1, null)
  println("main: thread1 joined")
}
