package cs4215.events

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._

import scala.concurrent.{ExecutionContext, Future, Promise}

object Events {
  private val onKeyPress = scala.collection.mutable.HashSet.empty[Key => Unit]

  def init(window: Long): Unit = {
    glfwSetKeyCallback(window, (_, glfwKey, _, _, _) => {
      if (glfwKey != GLFW_KEY_UNKNOWN)
        onKeyPress.toList.foreach(_(Key.fromGlfwKey(glfwKey)))
    })
  }

  def dispose(window: Long): Unit = {
    glfwFreeCallbacks(window)
  }

  def waitForKeyPress(keyPredicate: Key => Boolean)(implicit ec: ExecutionContext): Future[Key] = {
    val promise = Promise[Key]()
    val cb: Key => Unit = new Function1[Key, Unit] {
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
}
