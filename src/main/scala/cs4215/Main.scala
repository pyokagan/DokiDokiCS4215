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
    Scene += new Scene.ImageNode("sylvie blue normal.png") {
      pose.position.y = -10.0f
    }
    Scene += new Scene.ImageNode("textbox.png") {
      pose.position.z = 10.0f
      pose.position.y = -268.0f
    }
    Scene += new Scene.TextNode("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse euismod diam nec elit ultrices rhoncus. Nullam non facilisis est. Morbi tempor facilisis aliquet. Donec eu feugiat sapien.") {
      maxWidth = 700.0f / 0.3f
      pose.position.z = 20.0f
      pose.position.y = -220.0f
      pose.position.x = -360.0f
      pose.scale.x = 0.3f
      pose.scale.y = 0.3f
    }
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
