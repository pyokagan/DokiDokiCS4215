package cs4215

import org.lwjgl.glfw._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl._

object Main {
  var title = "The Question"
  private var window = 0L

  def main(args: Array[String]): Unit = {
    init()
    loop()
    dispose()
  }

  private def init(): Unit = {
    GLFWErrorCallback.createPrint(System.err).set()
    if (!glfwInit())
      throw new RuntimeException("Unable to initialize GLFW")
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    window = glfwCreateWindow(1280, 720, title, 0, 0)
    if (window == 0)
      throw new RuntimeException("Failed to create the GLFW window")
    glfwMakeContextCurrent(window)
    GL.createCapabilities
  }

  private def loop(): Unit = {
    while (!glfwWindowShouldClose(window)) {
      glfwSwapBuffers(window)
      glfwPollEvents()
    }
  }

  private def dispose(): Unit = {
    glfwDestroyWindow(window)
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }
}
