package cs4215

import cs4215.Util._
import cs4215.events.Events
import cs4215.game.Game
import cs4215.scene.Scene
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw._
import org.lwjgl.opengl._

import scala.concurrent.Future

object Main {
  val Title = "The Question"
  val TicksPerSecond = 60 // Number of logic ticks per second -- affects animation speed
  val TickPeriod = 1.0 / TicksPerSecond
  private var window = 0L

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
    Game.run()
  }

  private def loop(fut: Future[Unit]): Unit = withStack(stack => {
    val widthBuf = stack.callocInt(1)
    val heightBuf = stack.callocInt(1)
    var lastTick = glfwGetTime()

    while (!glfwWindowShouldClose(window) && !fut.isCompleted) {
      Events.runCallbacks()

      // Logic ticks
      val currentTime = glfwGetTime()
      while (lastTick + TickPeriod < currentTime) {
        Events.tick()
        lastTick += TickPeriod
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
}
