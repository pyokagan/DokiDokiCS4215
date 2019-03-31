package cs4215

import cs4215.Util._
import cs4215.events.Events
import cs4215.game.Game
import cs4215.scene.Scene
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw._
import org.lwjgl.opengl._

import scala.concurrent.{ExecutionContext, Future}

object Main {
  val Title = "The Question"
  private var window = 0L
  private val callbackQueue = scala.collection.mutable.Queue.empty[Runnable]

  def main(args: Array[String]): Unit = {
    val fut = init()
    loop(fut)
    dispose()
  }

  private def init(): Future[Unit] = {
    GLFWErrorCallback.createPrint(System.err).set()
    if (!glfwInit())
      throw new RuntimeException("Unable to initialize GLFW")
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    window = glfwCreateWindow(Scene.VirtualWidth, Scene.VirtualHeight, Title, 0, 0)
    if (window == 0)
      throw new RuntimeException("Failed to create the GLFW window")
    Events.init(window)
    glfwMakeContextCurrent(window)
    GL.createCapabilities()
    Game.run()(ExecutionContext.fromExecutor(enqueueCallback))
  }

  private def loop(fut: Future[Unit]): Unit = withStack(stack => {
    val widthBuf = stack.callocInt(1)
    val heightBuf = stack.callocInt(1)

    while (!glfwWindowShouldClose(window) && !fut.isCompleted) {
      // Run callbacks
      while (!callbackQueue.isEmpty) {
        val cb = callbackQueue.dequeue()
        cb.run()
      }

      // Render
      glfwGetWindowSize(window, widthBuf, heightBuf)
      Scene.render(widthBuf.get(0), heightBuf.get(0))
      glfwSwapBuffers(window)
      glfwPollEvents()
    }
  })

  private def dispose(): Unit = {
    Scene.dispose()
    Events.dispose(window)
    glfwDestroyWindow(window)
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }

  private def enqueueCallback(cb: Runnable): Unit = {
    callbackQueue.enqueue(cb)
  }
}
