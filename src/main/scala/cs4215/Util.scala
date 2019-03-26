package cs4215

import org.lwjgl.system.{MemoryStack, MemoryUtil}

private object Util {
  def withStack[T](f: MemoryStack => T): T = {
    val stack = MemoryStack.stackPush()
    try {
      f(stack)
    } finally {
      stack.close()
    }
  }

  def withMalloc[T](n: Int)(f: java.nio.ByteBuffer => T): T = {
    val x = MemoryUtil.memAlloc(n)
    try {
      f(x)
    } finally {
      MemoryUtil.memFree(x)
    }
  }

  def withMalloc[T](src: Array[Byte])(f: java.nio.ByteBuffer => T): T =
    withMalloc(src.length)(buf => {
      buf.put(src)
      buf.rewind()
      f(buf)
    })

  def withMallocShort[T](n: Int)(f: java.nio.ShortBuffer => T): T = {
    val x = MemoryUtil.memAllocShort(n)
    try {
      f(x)
    } finally {
      MemoryUtil.memFree(x)
    }
  }

  def readAllBytes(s: java.io.InputStream): Array[Byte] = {
    def aux(buf: scala.collection.mutable.ArrayBuffer[Byte], b: Int): Array[Byte] =
      if (b < 0) {
        buf.toArray
      } else {
        buf += b.toByte
        aux(buf, s.read)
      }
    aux(scala.collection.mutable.ArrayBuffer.empty[Byte], s.read)
  }

  def readResource(filename: String): Array[Byte] = {
    val stream = getClass().getResourceAsStream(filename)
    if (stream == null)
      throw new RuntimeException("No such resource: " + filename)
    readAllBytes(stream)
  }
}
