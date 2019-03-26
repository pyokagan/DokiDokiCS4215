package cs4215.scene

import org.joml._
import org.lwjgl.opengl.GL11C._
import org.lwjgl.opengl.GL15C._
import org.lwjgl.opengl.GL20C._
import org.lwjgl.opengl.GL30C._

/**
 * Renders a 2d image texture.
 */
private object RenderImage {
  lazy val program = new Program("""
    #version 130
    in vec2 aPos;
    in vec2 aUV;
    uniform mat4 uMVPMatrix;
    uniform vec2 uSize;
    out vec2 vUV;
    void main() {
      gl_Position = uMVPMatrix * vec4(aPos * uSize, 0.0, 1.0);
      vUV = aUV;
    }
  """, """
    #version 130
    in vec2 vUV;
    uniform sampler2D uTexture;
    uniform float uOpacity;
    void main() {
      gl_FragColor = texture(uTexture, vUV);
      gl_FragColor.a = clamp(gl_FragColor.a * uOpacity, 0.0f, 1.0f);
    }
  """)
  val vertexData: Array[Float] = Array(
    -0.5f, -0.5f, 0.0f, 1.0f, // a
    0.5f, -0.5f, 1.0f, 1.0f, // b
    0.5f, 0.5f, 1.0f, 0.0f, // c
    -0.5f, 0.5f, 0.0f, 0.0f, // d
  )
  lazy val arrayBuf = glGenBuffers()
  lazy val vao = {
    val vao = glGenVertexArrays()
    glBindVertexArray(vao)
    glBindBuffer(GL_ARRAY_BUFFER, arrayBuf)
    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW)
    val aPosLoc = program.getAttribLocation("aPos")
    glVertexAttribPointer(aPosLoc, 2, GL_FLOAT, false, 16, 0)
    glEnableVertexAttribArray(aPosLoc)
    val aUVLoc = program.getAttribLocation("aUV")
    glVertexAttribPointer(aUVLoc, 2, GL_FLOAT, false, 16, 8)
    glEnableVertexAttribArray(aUVLoc)
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, QuadElem.glBuf)
    glBindVertexArray(0)
    vao
  }

  def apply(tex: Texture, uMVPMatrix: Matrix4fc, uOpacity: Float): Unit = {
    glBindVertexArray(vao)
    program.use()
    program.setUniform("uMVPMatrix", uMVPMatrix)
    program.setUniform("uTexture", 0)
    program.setUniform("uOpacity", uOpacity)
    program.setUniform("uSize", tex.width, tex.height)
    tex.bind(0)
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0)
    glBindVertexArray(0)
  }

  def dispose(): Unit = {
    glDeleteVertexArrays(vao)
    glDeleteBuffers(arrayBuf)
    program.dispose()
  }
}
