package cs4215.scene

import org.joml._
import org.lwjgl.opengl.GL11C._

object Scene {
  val VirtualWidth = 1280
  val VirtualHeight = 720
  val AspectRatio = VirtualWidth.toFloat / VirtualHeight.toFloat
  val FrontZ = 1000f
  val BackZ = -1000f

  private val pMatrix = new Matrix4f().ortho(-VirtualWidth / 2f, VirtualWidth / 2f, -VirtualHeight / 2f, VirtualHeight / 2f, FrontZ, BackZ)
  private val mMatrix = new Matrix4f()
  private val mvpMatrix = new Matrix4f()
  private var sceneNodes = scala.collection.mutable.HashSet.empty[SceneNode]

  abstract class SceneNode {
    val pose: Pose
    var opacity: Float
    def render(mvpMatrix: Matrix4fc): Unit
  }

  class SquareNode(var color: Vector4f = new Vector4f(1f), val pose: Pose = new Pose(), var opacity: Float = 1f) extends SceneNode {
    def render(mvpMatrix: Matrix4fc): Unit =
      RenderSquare(mvpMatrix, color, opacity)
  }

  class ImageNode(var texture: Texture, var opacity: Float = 1.0f, val pose: Pose = new Pose()) extends SceneNode {
    def render(mvpMatrix: Matrix4fc): Unit =
      RenderImage(texture, mvpMatrix, opacity)
  }

  class SpriteNode(texture: Texture, opacity: Float = 1.0f, pose: Pose = new Pose()) extends ImageNode(texture, opacity, pose)

  class TextNode(var text: String = "", var maxWidth: Float = Float.PositiveInfinity, var font: Font = "OpenSans-Regular.ttf", var opacity: Float = 1.0f, val pose: Pose = new Pose()) extends SceneNode {
    def render(mvpMatrix: Matrix4fc): Unit =
      RenderText(font, mvpMatrix, this.opacity, text, maxWidth)
  }

  private def setViewportPreservingAspectRatio(width: Int, height: Int): Unit = {
    val aspectWH = AspectRatio
    val aspectHW = 1f / aspectWH
    val (viewW, viewH) = if (width > height) {
      val viewH = math.min(width * aspectHW, height)
      val viewW = viewH * aspectWH
      (viewW, viewH)
    } else {
      val viewW = math.min(height * aspectWH, width)
      val viewH = viewW * aspectHW
      (viewW, viewH)
    }
    val viewX = (width - viewW) / 2f
    val viewY = (height - viewH) / 2f
    glViewport(viewX.toInt, viewY.toInt, viewW.toInt, viewH.toInt)
  }

  def +=(sceneNode: SceneNode): Unit =
    sceneNodes += sceneNode

  def -=(sceneNode: SceneNode): Unit =
    sceneNodes -= sceneNode

  def render(viewportWidth: Int, viewportHeight: Int): Unit = {
    setViewportPreservingAspectRatio(viewportWidth, viewportHeight)
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    glDisable(GL_DEPTH_TEST)
    glDepthMask(false)
    glEnable(GL_BLEND)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    sceneNodes.toSeq.sorted(Ordering.by[SceneNode, Float](_.pose.position.z)).foreach(sceneNode => {
      sceneNode.pose.toMatrix4f(mMatrix)
      mvpMatrix.set(pMatrix).mul(mMatrix)
      sceneNode.render(mvpMatrix)
    })
  }

  def dispose(): Unit = {
    RenderSquare.dispose()
    RenderImage.dispose()
    RenderText.dispose()
    Texture.disposeAll()
    Font.disposeAll()
    QuadElem.dispose()
  }

  def clear(): Unit = {
    sceneNodes.clear()
  }

  def clearSprites(): Unit = {
    sceneNodes = sceneNodes.filter { case _: SpriteNode => false case _ => true }
  }
}
