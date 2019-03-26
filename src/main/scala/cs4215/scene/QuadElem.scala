package cs4215.scene

import cs4215.Util._
import org.lwjgl.opengl.GL15C._

private object QuadElem {
  val MaxQuads = 10922

  lazy val glBuf = withMallocShort(MaxQuads * 6)(indices => {
    for (i <- 0 until MaxQuads) {
      indices.put(6 * i, (4 * i).toShort)
      indices.put(6 * i + 1, (4 * i + 1).toShort)
      indices.put(6 * i + 2, (4 * i + 2).toShort)
      indices.put(6 * i + 3, (4 * i + 2).toShort)
      indices.put(6 * i + 4, (4 * i + 3).toShort)
      indices.put(6 * i + 5, (4 * i).toShort)
    }
    val buf = glGenBuffers()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buf)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)
    buf
  })

  def dispose(): Unit = {
    glDeleteBuffers(glBuf)
  }
}
