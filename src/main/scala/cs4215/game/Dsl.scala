package cs4215.game

import cs4215.events._
import cs4215.scene._

import scala.concurrent.{ExecutionContext, Future}

object Dsl {
  case class Character(name: String = "", color: String = "#ffffff")

  private lazy val msgNode = new Scene.TextNode {
    maxWidth = 700.0f / 0.25f
    pose.position.set(-360f, -250f, 20f)
    pose.scale.set(0.25f, 0.25f, 1f)
  }
  private lazy val textbox = new Scene.ImageNode("textbox") {
    pose.position.set(0f, -268f, 10f)
  }
  private lazy val charName = new Scene.TextNode {
    maxWidth = 700f / 0.28f
    pose.position.set(-380f, -205f, 20f)
    pose.scale.set(0.28f, 0.28f, 1f)
  }

  def say(char: Character, msg: String)(implicit ec: ExecutionContext): Future[Unit] = {
    for {
      _ <- {
        Scene += textbox
        Scene += charName
        Scene += msgNode
        msgNode.text = msg
        charName.text = char.name
        Events.waitForKeyPress(KeySpace)
      }
      _ <- {
        Scene -= charName
        Scene -= msgNode
        Scene -= textbox
        Events.nextEvent
      }
    } yield()
  }

  def say(charName: String, msg: String)(implicit ec: ExecutionContext): Future[Unit] = {
    say(new Character(charName), msg)
  }

  def say(msg: String)(implicit ec: ExecutionContext): Future[Unit] = {
    say(new Character(), msg)
  }

  def scene(texture: Texture)(implicit ec: ExecutionContext): Future[Unit] = {
    val bg = new Scene.ImageNode(texture)
    bg.pose.position.z = -100.0f
    Scene.clear()
    Scene += bg
    Events.nextEvent()
  }

  def sceneBlack()(implicit ec: ExecutionContext): Future[Unit] = {
    Scene.clear()
    Events.nextEvent()
  }

  def show(texture: Texture)(implicit ec: ExecutionContext): Future[Unit] = {
    val node = new Scene.SpriteNode(texture)
    for {
      _ <- {
        Scene.clearSprites()
        Scene += node
        Events.nextEvent
      }
    } yield()
  }
}
