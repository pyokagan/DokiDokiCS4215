package cs4215.scene

import cs4215.Util._
import org.lwjgl.opengl.GL11C._
import org.lwjgl.opengl.GL12C._
import org.lwjgl.opengl.GL13C._
import org.lwjgl.opengl.GL30C._
import org.lwjgl.stb.STBImage._

import scala.language.implicitConversions

/**
 * An OpenGL 2d texture.
 */
final class Texture(private val glTexture: Int, val width: Int, val height: Int) {
  private[scene] def bind(slot: Int): Unit = {
    glActiveTexture(GL_TEXTURE0 + slot)
    glBindTexture(GL_TEXTURE_2D, glTexture)
  }

  private[scene] def dispose(): Unit = {
    glDeleteTextures(glTexture)
  }
}

object Texture {
  val ImageSuffixes = Seq("", ".png", ".jpg")

  private val textures = scala.collection.mutable.HashMap.empty[String, Texture]

  def apply(s: String): Texture =
    textures.getOrElseUpdate(s, make2d(readResource(findResource(ImageSuffixes)(s))))

  private def make2d(data: Array[Byte]): Texture = withMalloc(data)(make2d)
  private def make2d(data: java.nio.ByteBuffer): Texture = withStack(stack => {
    assert(data.isDirect)
    val x = stack.callocInt(1)
    val y = stack.callocInt(1)
    val channels_in_file = stack.callocInt(1)
    val imageData = stbi_load_from_memory(data, x, y, channels_in_file, 4)
    try {
      val glTexture = glGenTextures()
      if (glTexture == 0)
        throw new RuntimeException("glGenTextures failed")
      glActiveTexture(GL_TEXTURE0)
      glBindTexture(GL_TEXTURE_2D, glTexture)
      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, x.get(0), y.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData)
      glGenerateMipmap(GL_TEXTURE_2D)
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
      new Texture(glTexture, x.get(0), y.get(0))
    } finally {
      stbi_image_free(imageData)
    }
  })

  def disposeAll(): Unit = {
    textures.foreach { case (_, v) => v.dispose() }
    textures.clear()
  }

  implicit def string2Texture(s: String): Texture = Texture(s)
}
