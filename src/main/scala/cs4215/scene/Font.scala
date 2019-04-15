package cs4215.scene

import cs4215.Util._
import org.lwjgl.opengl.GL11C._
import org.lwjgl.opengl.GL12C._
import org.lwjgl.opengl.GL13C._
import org.lwjgl.opengl.GL30C._
import org.lwjgl.stb.STBTTFontinfo
import org.lwjgl.stb.STBTruetype._
import org.lwjgl.system.MemoryUtil

import scala.language.implicitConversions

final class Font(val texture: Texture, val charMap: Map[Char, Font.CharInfo], val height: Float) {
  private[scene] def dispose(): Unit = {
    texture.dispose()
  }
}

object Font {
  final case class CharInfo(x: Int, y: Int, w: Int, h: Int, xoff: Int, yoff: Int, advance: Float)

  val TexWidth = 1024
  val TexHeight = 1024
  val SdfSize = 128.0f // the larger this is, the better large font sizes look
  val PixelDistScale = 20.0f // Trades off precision with ability to handle *smaller* sizes
  val OneEdgeValue = 128.toByte
  val Padding = 20
  val Characters = {
    val sb = new java.lang.StringBuilder()
    for (i <- 32 until 127)
      sb.appendCodePoint(i)
    sb.toString
  }

  private val fonts = scala.collection.mutable.HashMap.empty[String, Font]

  def apply(s: String): Font =
    fonts.getOrElseUpdate(s, makeFont(readResource(s)))

  private def makeFont(data: Array[Byte]): Font =
    withMalloc(data)(makeFont)

  private def makeFont(data: java.nio.ByteBuffer): Font = withStack(stack => withCalloc(TexWidth * TexHeight)(texMem => {
    assert(data.isDirect)
    val fontInfo = STBTTFontinfo.malloc()
    stbtt_InitFont(fontInfo, data)
    val binPack = new BinPack(TexWidth, TexHeight)
    val outWidth = stack.callocInt(1)
    val outHeight = stack.callocInt(1)
    val outXoff = stack.callocInt(1)
    val outYoff = stack.callocInt(1)
    val outAdvance = stack.callocInt(1)
    val scale = stbtt_ScaleForPixelHeight(fontInfo, SdfSize)
    var charMap = Map.empty[Char, CharInfo]

    Characters.foreach(c => {
      stbtt_GetCodepointHMetrics(fontInfo, c.toInt, outAdvance, null)
      val outSdf = stbtt_GetCodepointSDF(fontInfo, scale, c.toInt, Padding, OneEdgeValue, PixelDistScale, outWidth, outHeight, outXoff, outYoff)
      if (outSdf != null) {
        val (dstX, dstY, _, _) = binPack.pack(outWidth.get(0) + 1, outHeight.get(0) + 1)
        blit(texMem, outSdf, TexWidth, dstX, dstY, outWidth.get(0), outHeight.get(0))
        stbtt_FreeSDF(outSdf)
        charMap = charMap + (c -> CharInfo(dstX, dstY, outWidth.get(0), outHeight.get(0), outXoff.get(0), outYoff.get(0), outAdvance.get(0) * scale))
      } else {
        charMap = charMap + (c -> CharInfo(0, 0, 0, 0, 0, 0, outAdvance.get(0) * scale))
      }
    })
    // dump("dump.png", texMem, TexWidth, TexHeight)
    fontInfo.free()

    // Upload texture
    val glTexture = glGenTextures()
    glActiveTexture(GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, glTexture)
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, TexWidth, TexHeight, 0, GL_RED, GL_UNSIGNED_BYTE, texMem)
    glGenerateMipmap(GL_TEXTURE_2D)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
    new Font(new Texture(glTexture, TexWidth, TexHeight), charMap, SdfSize)
  }))

  private def blit(dst: java.nio.ByteBuffer, src: java.nio.ByteBuffer, dstWidth: Int, dstX: Int, dstY: Int, srcWidth: Int, srcHeight: Int): Unit = {
    val dstAddr = MemoryUtil.memAddress(dst)
    val srcAddr = MemoryUtil.memAddress(src)
    for (srcY <- 0 until srcHeight)
      MemoryUtil.memCopy(srcAddr + (srcY * srcWidth), dstAddr + ((srcY + dstY) * dstWidth) + dstX, srcWidth)
  }

  // For debugging
  private def dump(dst: String, src: java.nio.ByteBuffer, width: Int, height: Int): Unit = withStack(stack => {
    import org.lwjgl.stb.STBImageWrite._
    stbi_write_png(stack.UTF8(dst), width, height, 1, src, width)
  })

  private[scene] def disposeAll(): Unit = {
    fonts.foreach { case (_, v) => v.dispose() }
    fonts.clear()
  }

  implicit def string2Font(s: String): Font = Font(s)
}
