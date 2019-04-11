package cs4215.scene

import org.joml._
import org.lwjgl.opengl.GL11C._
import org.lwjgl.opengl.GL15C._
import org.lwjgl.opengl.GL20C._
import org.lwjgl.opengl.GL30C._
import org.lwjgl.system.MemoryUtil

/**
 * Renders text.
 */
private object RenderText {
  lazy val program = new Program("""
    #version 130
    in vec2 aPos;
    in vec2 aUV;
    uniform mat4 uMVPMatrix;
    out vec2 vUV;
    void main() {
      gl_Position = uMVPMatrix * vec4(aPos, 0.0f, 1.0f);
      vUV = aUV;
    }
  """, """
    #version 130
    in vec2 vUV;
    uniform sampler2D uTexture;
    uniform float uOpacity;
    void main() {
      float a = texture(uTexture, vUV).r;
      gl_FragColor = vec4(1.0f, 1.0f, 1.0f, floor(a + 0.6f));
      float w = fwidth(a);
      gl_FragColor.a *= smoothstep(0.5 - w, 0.5 + w, a);
      gl_FragColor.a *= uOpacity;
    }
  """)
  val buffers = scala.collection.mutable.ArrayBuffer.empty[(Int, Int, java.nio.FloatBuffer)]
  var numBuffersUsed = 0

  def reset(): Unit = {
    numBuffersUsed = 0
  }

  def put(i: Int, x: Float): Unit = {
    val (glVao, glBuf, buf) = buffers(i)
    if (buf.remaining <= 0) {
      val prevPosition = buf.position()
      val newBuf = MemoryUtil.memRealloc(buf, (buf.capacity * 3) / 2)
      newBuf.position(prevPosition)
      buffers.update(i, (glVao, glBuf, newBuf))
      newBuf.put(x)
    } else buf.put(x)
  }

  def putVert(i: Int, aPos_x: Float, aPos_y: Float, aUV_x: Float, aUV_y: Float): Unit = {
    put(i, aPos_x)
    put(i, aPos_y)
    put(i, aUV_x)
    put(i, aUV_y)
  }

  def putChar(buf: Int, font: Font, x: Float, y: Float, c: Char): (Float, Float) = {
    font.charMap.get(c).map(charInfo => {
      val posX0 = x + charInfo.xoff
      val posY0 = y - charInfo.yoff
      val uvX0 = charInfo.x / Font.TexWidth.toFloat
      val uvY0 = charInfo.y / Font.TexHeight.toFloat
      val posX1 = posX0 + charInfo.w
      val posY1 = posY0 - charInfo.h
      val uvX1 = (charInfo.x + charInfo.w) / Font.TexWidth.toFloat
      val uvY1 = (charInfo.y + charInfo.h) / Font.TexHeight.toFloat
      putVert(buf, posX0, posY0, uvX0, uvY0)
      putVert(buf, posX0, posY1, uvX0, uvY1)
      putVert(buf, posX1, posY1, uvX1, uvY1)
      putVert(buf, posX1, posY0, uvX1, uvY0)
      (x + charInfo.advance, y)
    }).getOrElse((x, y))
  }

  def putWord(buf: Int, font: Font, maxWidth: Float, needSpace: Boolean, x: Float, y: Float, word: String): (Float, Float) = {
    val spaceWidth = if (needSpace) font.charMap.get(' ').map(_.advance).get else 0f
    val wordWidth = word.map(font.charMap.get(_).map(_.advance).getOrElse(0f)).sum
    val needLineBreak = needSpace && x + spaceWidth + wordWidth > maxWidth
    val (startX, startY) = if (needLineBreak) (0f, y - font.height) else (x + spaceWidth, y)
    val (newX, newY) = word.foldLeft((startX, startY)){ case ((x, y), c) => putChar(buf, font, x, y, c) }
    (newX, newY)
  }

  def apply(font: Font, uMVPMatrix: Matrix4fc, opacity: Float, text: String, maxWidth: Float = Float.PositiveInfinity): Unit = {
    if (numBuffersUsed >= buffers.length) {
      val glVao = glGenVertexArrays()
      val glBuf = glGenBuffers()
      glBindVertexArray(glVao)
      glBindBuffer(GL_ARRAY_BUFFER, glBuf)
      val aPosLoc = program.getAttribLocation("aPos")
      glVertexAttribPointer(aPosLoc, 2, GL_FLOAT, false, 16, 0)
      glEnableVertexAttribArray(aPosLoc)
      val aUVLoc = program.getAttribLocation("aUV")
      glVertexAttribPointer(aUVLoc, 2, GL_FLOAT, false, 16, 8)
      glEnableVertexAttribArray(aUVLoc)
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, QuadElem.glBuf)
      glBindVertexArray(0)
      buffers.append((glVao, glBuf, MemoryUtil.memAllocFloat(64)))
    }
    val idx = numBuffersUsed
    numBuffersUsed += 1
    text.split("\\s+").foldLeft((false, 0f, 0f)){ case ((needSpace, x, y), word) =>
      val (newX, newY) = putWord(idx, font, maxWidth, needSpace, x, y, word)
      (true, newX, newY)
    }
    val (glVao, _, buf) = buffers(idx)
    glBindVertexArray(glVao)
    val bufPos = buf.position()
    buf.rewind()
    glBufferData(GL_ARRAY_BUFFER, MemoryUtil.memSlice(buf, 0, bufPos), GL_DYNAMIC_DRAW)
    program.use()
    program.setUniform("uMVPMatrix", uMVPMatrix)
    font.texture.bind(0)
    program.setUniform("uTexture", 0)
    program.setUniform("uOpacity", opacity)
    glDrawElements(GL_TRIANGLES, bufPos / 4 * 6, GL_UNSIGNED_SHORT, 0)
    glBindVertexArray(0)
  }

  def dispose(): Unit = {
    buffers.foreach { case (glVao, glBuf, buf) =>
      glDeleteVertexArrays(glVao)
      glDeleteBuffers(glBuf)
      MemoryUtil.memFree(buf)
    }
    program.dispose()
  }
}
