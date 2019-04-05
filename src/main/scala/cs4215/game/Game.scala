package cs4215.game

import cs4215.events._
import cs4215.scene.{Scene, Texture}

import scala.concurrent.{ExecutionContext, Future, Promise}

/**
 * Main entry point for the game.
 */
object Game {

  class Character(val name: String = "", var color: String = "#ffffff")

  def say(char: Character = new Character(), msg: String)(implicit ec: ExecutionContext): Future[Unit] = {
    val msgNode = new Scene.TextNode(msg)
    msgNode.maxWidth = 700.0f / 0.25f
    msgNode.pose.position.z = 20.0f
    msgNode.pose.position.y = -250.0f
    msgNode.pose.position.x = -360.0f
    msgNode.pose.scale.x = 0.25f
    msgNode.pose.scale.y = 0.25f

    //ToDo: Consider optimising textbox addition and removal
    val textbox = new Scene.ImageNode("textbox.png")
    textbox.pose.position.z = 10.0f
    textbox.pose.position.y = -268.0f

    //ToDo: Implement character color customisation
    val charName = new Scene.TextNode(char.name)
    charName.maxWidth = 700.0f / 0.28f
    charName.pose.position.z = 20.0f
    charName.pose.position.y = -205.0f
    charName.pose.position.x = -380.0f
    charName.pose.scale.x = 0.28f
    charName.pose.scale.y = 0.28f

    for {
      _ <- {
        Scene += textbox
        Scene += charName
        Scene += msgNode
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

  def scene(texture: Texture)(implicit ec: ExecutionContext): Future[Unit] = {
    val bg = new Scene.ImageNode(texture)
    bg.pose.position.z = -100.0f

    for {
      _ <- {
        Scene.clear()
        Scene += bg
        Events.nextEvent
      }
    } yield()

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


  def run()(implicit ec: ExecutionContext): Future[Unit] = {

    val testChar = new Character("Sylvie", "#abcdef")

    for {
      _ <- scene("bg uni.jpg")
      _ <- say("Test1", "Veni Vidi Vici")
      _ <- say (msg = "Hello World!")

      _ <- scene("bg meadow.jpg")
      _ <- say ("Test3", "In vino veritas")
      _ <- say ("Me4", "Hey... Umm...")
      _ <- show("sylvie blue giggle.png")
      _ <- say(testChar, msg = "She giggles.")
      _ <- show("sylvie green normal.png")
      _ <- say(testChar, msg = "She's in green.")

      _ <- scene("bg club.jpg")
      _ <- say("Test7", "Ad Astra")

      _ <- Events.waitForKeyPress(KeyEscape)
    } yield()
  }



}
