package cs4215.events

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._

import scala.concurrent.{ExecutionContext, Future, Promise}

object Events {
  private val onKeyPress = scala.collection.mutable.HashSet.empty[Key => Unit]
  private val onMouseButtonPress = scala.collection.mutable.HashSet.empty[MouseButton => Unit]

  def init(window: Long): Unit = {
    glfwSetKeyCallback(window, (_, glfwKey, _, action, _) => {
      if (glfwKey != GLFW_KEY_UNKNOWN && action == GLFW_PRESS)
        onKeyPress.toList.foreach(_(Key.fromGlfwKey(glfwKey)))
    })
    glfwSetMouseButtonCallback(window, (_, glfwButton, action, _) => {
      if (action == GLFW_PRESS)
        onMouseButtonPress.toList.foreach(_(MouseButton.fromGlfwMouseButton(glfwButton)))
    })
  }

  def dispose(window: Long): Unit = {
    glfwFreeCallbacks(window)
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

  /** Returns empty future to advance event*/
  def nextEvent()(implicit  ec: ExecutionContext): Future[Unit] = {
    val promise = Promise[Unit]()
    promise.success()
    promise.future
  }
}
