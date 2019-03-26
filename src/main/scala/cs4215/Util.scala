package cs4215

import org.lwjgl.system.MemoryStack

private object Util {
  def withStack[T](f: MemoryStack => T): T = {
    val stack = MemoryStack.stackPush()
    try {
      f(stack)
    } finally {
      stack.close()
    }
  }
}
