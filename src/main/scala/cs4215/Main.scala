package cs4215

import cs4215.Util._
import cs4215.scene.Scene
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw._
import org.lwjgl.opengl._

object Main {
  val Title = "The Question"
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
    window = glfwCreateWindow(Scene.VirtualWidth, Scene.VirtualHeight, Title, 0, 0)
    if (window == 0)
      throw new RuntimeException("Failed to create the GLFW window")
    glfwMakeContextCurrent(window)
    GL.createCapabilities()

    // TODO: Code to test Scene. To be removed.
    Scene += new Scene.ImageNode("bg uni.jpg") {
      pose.position.z = -10.0f
    }
    Scene += new Scene.TextNode("Hello World!", 0f)
  }

  private def loop(): Unit = withStack(stack => {
    val widthBuf = stack.callocInt(1)
    val heightBuf = stack.callocInt(1)

    while (!glfwWindowShouldClose(window)) {
      glfwGetWindowSize(window, widthBuf, heightBuf)
      Scene.render(widthBuf.get(0), heightBuf.get(0))
      glfwSwapBuffers(window)
      glfwPollEvents()
    }
  })

  private def dispose(): Unit = {
    Scene.dispose()
    glfwDestroyWindow(window)
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }
}
