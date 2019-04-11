package cs4215.events

import org.lwjgl.glfw.GLFW._

sealed trait MouseButton
case object MouseButtonLeft extends MouseButton
case object MouseButtonMiddle extends MouseButton
case object MouseButtonRight extends MouseButton
case object MouseButton4 extends MouseButton
case object MouseButton5 extends MouseButton
case object MouseButton6 extends MouseButton
case object MouseButton7 extends MouseButton
case object MouseButton8 extends MouseButton

object MouseButton {
  def fromGlfwMouseButton(glfwMouseButton: Int): MouseButton = glfwMouseButton match {
    case GLFW_MOUSE_BUTTON_LEFT => MouseButtonLeft
    case GLFW_MOUSE_BUTTON_MIDDLE => MouseButtonMiddle
    case GLFW_MOUSE_BUTTON_RIGHT => MouseButtonRight
    case GLFW_MOUSE_BUTTON_4 => MouseButton4
    case GLFW_MOUSE_BUTTON_5 => MouseButton5
    case GLFW_MOUSE_BUTTON_6 => MouseButton6
    case GLFW_MOUSE_BUTTON_7 => MouseButton7
    case GLFW_MOUSE_BUTTON_8 => MouseButton8
    case _ => throw new AssertionError("unexpected glfw mouse button: " + glfwMouseButton)
  }
}
