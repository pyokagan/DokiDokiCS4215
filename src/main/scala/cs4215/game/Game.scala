package cs4215.game

import cs4215.events._
import cs4215.scene.{Scene, Texture}

import scala.concurrent.{ExecutionContext, Future, Promise}

/**
 * Main entry point for the game.
 */
object Game {

  class Character(val name: String = "", var color: String = "#ffffff")

  // Declare characters used by this game.
  val s = new Character("Sylvie", color = "#abcdef")
  val m = new Character("Me", color = "#c8c8ff")

  def say(char: Character, msg: String)(implicit ec: ExecutionContext): Future[Unit] = {
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

  // The game starts here.
  def run()(implicit ec: ExecutionContext): Future[Unit] = for {
    _ <- scene("bg uni.jpg")
    _ <- say("When we come out of the university, I spot her right away.")
    _ <- show("sylvie green normal.png")
    _ <- say("Sylvie's got a big heart and she's always been a good friend to me.")
    _ <- {
      // TODO: Menu
      Events.nextEvent()
    }
    _ <- rightaway()
  } yield ()

  def rightaway()(implicit ec: ExecutionContext): Future[Unit] = for {
    _ <- show("sylvie green smile.png")
    _ <- say(m, "Are you going home now? Wanna walk back with me?")
    _ <- say(s, "Why not?")
    _ <- sceneBlack()
    _ <- say("Good Ending.")
  } yield ()

  def later()(implicit ec: ExecutionContext): Future[Unit] = for {
    _ <- say("I can't get up the nerve to ask right now. With a gulp, I decide to ask her later.")
    _ <- sceneBlack()
    _ <- say("Bad Ending.")
  } yield ()

}
