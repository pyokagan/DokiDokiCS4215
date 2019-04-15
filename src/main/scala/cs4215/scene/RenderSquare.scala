package cs4215.scene

import org.joml._
import org.lwjgl.opengl.GL11C._
import org.lwjgl.opengl.GL15C._
import org.lwjgl.opengl.GL20C._
import org.lwjgl.opengl.GL30C._

/**
 * Renders a 1x1 square.
 */
private object RenderSquare {
  lazy val program = new Program("""
    #version 130
    in vec2 aPos;
    uniform mat4 uMVPMatrix;
    void main() {
      gl_Position = uMVPMatrix * vec4(aPos, 0.f, 1.f);
    }
  """, """
    #version 130
    uniform vec4 uColor;
    uniform float uOpacity;
    void main() {
      gl_FragColor = vec4(uColor.rgb, uColor.a * uOpacity);
    }
  """)
  private val vertexData: Array[Float] = Array(
    -0.5f, -0.5f, // a
    0.5f, -0.5f, // b
    0.5f, 0.5f, // c
    -0.5f, 0.5f, // d
  )
  lazy val arrayBuf = glGenBuffers()
  lazy val vao = {
    val vao = glGenVertexArrays()
    glBindVertexArray(vao)
    glBindBuffer(GL_ARRAY_BUFFER, arrayBuf)
    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW)
    val aPosLoc = program.getAttribLocation("aPos")
    glVertexAttribPointer(aPosLoc, 2, GL_FLOAT, false, 8, 0)
    glEnableVertexAttribArray(aPosLoc)
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, QuadElem.glBuf)
    glBindVertexArray(0)
    vao
  }

  def apply(uMVPMatrix: Matrix4fc, uColor: Vector4fc, uOpacity: Float): Unit = {
    glBindVertexArray(vao)
    program.use()
    program.setUniform("uMVPMatrix", uMVPMatrix)
    program.setUniform("uColor", uColor)
    program.setUniform("uOpacity", uOpacity)
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0)
    glBindVertexArray(0)
  }

  def dispose(): Unit = {
    glDeleteVertexArrays(vao)
    glDeleteBuffers(arrayBuf)
    program.dispose()
  }
}
