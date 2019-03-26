package cs4215.scene

import cs4215.Util._
import org.joml._
import org.lwjgl.opengl.GL20C._

/**
 * Represents an OpenGL Program.
 */
private final class Program(private val vertShader: Int, private val fragShader: Int, private val glProgram: Int) {
  def this(x: (Int, Int, Int)) = this(x._1, x._2, x._3)
  def this(vertShader: String, fragShader: String) = this(Program.make(vertShader, fragShader))

  def getAttribLocation(name: String): Int = {
    val loc = glGetAttribLocation(glProgram, name)
    if (loc >= 0) loc else throw new IllegalStateException("no such attrib: " + name)
  }

  def getUniformLocation(name: String): Int = {
    val loc = glGetUniformLocation(glProgram, name)
    if (loc >= 0) loc else throw new IllegalStateException("no such uniform: " + name)
  }

  def use(): Unit = glUseProgram(glProgram)

  def setUniform(name: String, v: Int): Unit =
    glUniform1i(getUniformLocation(name), v)

  def setUniform(name: String, v: Float): Unit =
    glUniform1f(getUniformLocation(name), v)

  def setUniform(name: String, v1: Float, v2: Float): Unit =
    glUniform2f(getUniformLocation(name), v1, v2)

  def setUniform(name: String, v1: Float, v2: Float, v3: Float): Unit =
    glUniform3f(getUniformLocation(name), v1, v2, v3)

  def setUniform(name: String, v1: Float, v2: Float, v3: Float, v4: Float): Unit =
    glUniform4f(getUniformLocation(name), v1, v2, v3, v4)

  def setUniform(name: String, v: Vector2fc): Unit =
    setUniform(name, v.x(), v.y())

  def setUniform(name: String, v: Vector3fc): Unit =
    setUniform(name, v.x(), v.y(), v.z())

  def setUniform(name: String, v: Vector4fc): Unit =
    setUniform(name, v.x(), v.y(), v.z(), v.w())

  def setUniform(name: String, v: Matrix4fc): Unit = withStack(stack => {
    val mem = stack.mallocFloat(16)
    v.get(mem)
    glUniformMatrix4fv(getUniformLocation(name), false, mem)
  })

  def dispose(): Unit = {
    glDeleteProgram(glProgram)
    glDeleteShader(vertShader)
    glDeleteShader(fragShader)
  }
}

private object Program {
  private def makeShader(typ: Int, src: String): Int = {
    val shader = glCreateShader(typ)
    if (shader == 0)
      throw new IllegalStateException("glCreateShader failed")
    glShaderSource(shader, src)
    glCompileShader(shader)
    if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
      val infoLog = glGetShaderInfoLog(shader)
      glDeleteShader(shader)
      throw new IllegalStateException("glCompileShader failed: " + infoLog)
    }
    shader
  }

  private def makeProgram(vertShader: Int, fragShader: Int): Int = {
    val program = glCreateProgram()
    if (program == 0)
      throw new IllegalStateException("glCreateProgram failed")
    glAttachShader(program, vertShader)
    glAttachShader(program, fragShader)
    glLinkProgram(program)
    if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
      val infoLog = glGetProgramInfoLog(program)
      glDeleteProgram(program)
      throw new IllegalStateException("glLinkProgram failed: " + infoLog)
    }
    program
  }

  private def make(vertShader: String, fragShader: String): (Int, Int, Int) = {
    val glVertShader = makeShader(GL_VERTEX_SHADER, vertShader)
    val glFragShader = makeShader(GL_FRAGMENT_SHADER, fragShader)
    val glProgram = makeProgram(glVertShader, glFragShader)
    (glVertShader, glFragShader, glProgram)
  }
}
