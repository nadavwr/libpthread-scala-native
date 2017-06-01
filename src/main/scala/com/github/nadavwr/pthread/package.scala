package com.github.nadavwr

import scala.scalanative.native._

package object pthread {
  type pthread_t = Ptr[CStruct0]
  type pthread_attr_t = CStruct0

  @extern
  @link("pthread")
  object ffi {
    def pthread_create(thread: Ptr[pthread_t], 
                       attr: Ptr[pthread_attr_t],
                       start_routine: CFunctionPtr1[Ptr[Byte], Ptr[Byte]], 
                       arg: Ptr[Byte]): CInt = extern

    def pthread_join(thread: pthread_t, 
                     value_ptr: Ptr[Ptr[Byte]]): CInt = extern
  }

  /** don't forget to pthread_join()! */
  def pthread_create(thread: Ptr[pthread_t], 
                     attr: Ptr[pthread_attr_t],
                     start_routine: CFunctionPtr1[Ptr[Byte], Ptr[Byte]], 
                     arg: Ptr[Byte]): CInt =
    ffi.pthread_create(thread, attr, start_routine, arg)

  def pthread_join(thread: pthread_t, 
                   value_ptr: Ptr[Ptr[Byte]]): CInt =
    ffi.pthread_join(thread, value_ptr)

  val EPERM = 1
  val EINVAL = 22
  val EAGAIN = 35

  object PThread {
    val threadFunc: CFunctionPtr1[Ptr[Byte], Ptr[Byte]] =
      (p: Ptr[Byte]) => {
        p.cast[PThread].run()
        null
      }

    def apply(thunk: => Unit): PThread = {
      new PThread(thunk)
    }

    /** don't forget to join()! */
    def start(thunk: => Unit): PThread = {
      val thread = PThread(thunk)
      thread.start()
      thread
    }
  }

  class PThread(f: => Unit) {
    def run(): Unit = f

    private[PThread] val threadPtr: Ptr[pthread_t] =
      stdlib.malloc(sizeof[pthread_t]).cast[Ptr[pthread_t]]

    /** don't forget to join()! */
    def start(): Unit = {
      val result = 
        pthread_create(
          thread = threadPtr, 
          attr = null, 
          start_routine = PThread.threadFunc, 
          arg = this.cast[Ptr[Byte]])

      result match {
        case EPERM => throw new RuntimeException("EPERM")
        case EINVAL => throw new RuntimeException("EINVAL")
        case EAGAIN => throw new RuntimeException("EAGAIN")
        case 0 =>
        case n => throw new RuntimeException(s"unexpected return code $n")
      }
    }

    def join(): Unit = {
      pthread.pthread_join(
        thread = !threadPtr, 
        value_ptr = null).ensuring(_ == 0)

      stdlib.free(threadPtr.cast[Ptr[Byte]])
    }
  }
}
