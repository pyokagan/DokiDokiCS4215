package cs4215.events

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._

import scala.concurrent.{ExecutionContext, Future, Promise}

object Events {
  private val callbackQueue = scala.collection.mutable.Queue.empty[Runnable]
  private val onKeyPress = scala.collection.mutable.HashSet.empty[Key => Unit]
  private val onMouseButtonPress = scala.collection.mutable.HashSet.empty[MouseButton => Unit]
  private val onTick = scala.collection.mutable.HashSet.empty[() => Unit]

  def init(window: Long): Unit = {
    glfwSetKeyCallback(window, (_, glfwKey, _, action, _) => {
      if (glfwKey != GLFW_KEY_UNKNOWN && action == GLFW_PRESS)
        onKeyPress.toList.foreach(cb => {
          cb(Key.fromGlfwKey(glfwKey))
          runCallbacks()
        })
    })
    glfwSetMouseButtonCallback(window, (_, glfwButton, action, _) => {
      if (action == GLFW_PRESS)
        onMouseButtonPress.toList.foreach(cb => {
          cb(MouseButton.fromGlfwMouseButton(glfwButton))
          runCallbacks()
        })
    })
  }

  def dispose(window: Long): Unit = {
    glfwFreeCallbacks(window)
  }

  def enqueueCallback(cb: Runnable): Unit = {
    callbackQueue.enqueue(cb)
  }

  def runCallbacks(): Unit = {
    while (callbackQueue.nonEmpty) {
      val cb = callbackQueue.dequeue()
      cb.run()
    }
  }

  def tick(): Unit = {
    onTick.toList.foreach(cb => {
      cb()
      runCallbacks()
    })
  }

  def waitForKeyPress(keyPredicate: Key => Boolean)(implicit ec: ExecutionContext): Future[Key] = {
    val promise = Promise[Key]()
    val cb = new Function1[Key, Unit] {
      def apply(key: Key): Unit = {
        if (keyPredicate(key)) {
          onKeyPress -= this
          promise.success(key)
        }
      }
    }
    onKeyPress += cb
    promise.future
  }

  def waitForKeyPress(key: Key)(implicit ec: ExecutionContext): Future[Key] =
    waitForKeyPress(_ == key)

  def waitForKeyPress()(implicit ec: ExecutionContext): Future[Key] =
    waitForKeyPress(_ => true)

  def waitForMouseButtonPress(mouseButtonPredicate: MouseButton => Boolean)(implicit ec: ExecutionContext): Future[MouseButton] = {
    val promise = Promise[MouseButton]()
    val cb = new Function1[MouseButton, Unit] {
      def apply(mouseButton: MouseButton): Unit = {
        if (mouseButtonPredicate(mouseButton)) {
          onMouseButtonPress -= this
          promise.success(mouseButton)
        }
      }
    }
    onMouseButtonPress += cb
    promise.future
  }

  def waitForMouseButtonPress(mouseButton: MouseButton)(implicit ec: ExecutionContext): Future[MouseButton] =
    waitForMouseButtonPress(_ == mouseButton)

  def waitForMouseButtonPress()(implicit ex: ExecutionContext): Future[MouseButton] =
    waitForMouseButtonPress(_ => true)

  def waitForTick(numTicks: Int = 1)(implicit ec: ExecutionContext): Future[Unit] = {
    if (numTicks <= 0) {
      nextEvent()
    } else {
      val promise = Promise[Unit]()
      val cb = new Function0[Unit] {
        var ticks = numTicks
        def apply(): Unit = {
          ticks -= 1
          if (ticks <= 0) {
            onTick -= this
            promise.success()
          }
        }
      }
      onTick += cb
      promise.future
    }
  }

  /** Returns empty future to advance event*/
  def nextEvent()(implicit  ec: ExecutionContext): Future[Unit] = {
    val promise = Promise[Unit]()
    promise.success()
    promise.future
  }
}
