package cs4215.game

import cs4215.events._
import cs4215.scene.{Scene, Texture}

import scala.concurrent.{ExecutionContext, Future, Promise}

/**
 * Main entry point for the game.
 */
object Game {

  def say(msg: String)(implicit ec: ExecutionContext): Future[Unit] = {
    val msgNode = new Scene.TextNode(msg)
    msgNode.maxWidth = 700.0f / 0.3f
    msgNode.pose.position.z = 20.0f
    msgNode.pose.position.y = -220.0f
    msgNode.pose.position.x = -360.0f
    msgNode.pose.scale.x = 0.3f
    msgNode.pose.scale.y = 0.3f

    //ToDo: Consider optimising textbox addition and removal
    val textbox = new Scene.ImageNode("textbox.png")
    textbox.pose.position.z = 10.0f
    textbox.pose.position.y = -268.0f

    for {
      _ <- {
        Scene += textbox
        Events.nextEvent
      }
      _ <- {
        Scene += msgNode
        Events.waitForKeyPress(KeySpace)
      }
      _ <- {
        Scene -= msgNode
        Events.nextEvent
      }
      _ <- {
        Scene -= textbox
        Events.nextEvent
      }
    } yield()
  }

  def scene(texture: Texture)(implicit ec: ExecutionContext): Future[Unit] = {
    Scene.clear
    val node = new Scene.ImageNode(texture)
    //ToDo: Implement bg depth
    for {
      _ <- {
        Scene += node
        Events.nextEvent
      }
    } yield()

  }

  def show(texture: Texture)(implicit ec: ExecutionContext): Future[Unit] = {
    val node = new Scene.ImageNode(texture)
    //ToDo: Implement sprite removal and layering
    for {
      _ <- {
        Scene += node
        Events.nextEvent
      }
    } yield()

  }


  def run()(implicit ec: ExecutionContext): Future[Unit] = {
    // TODO: Code to test Scene. To be removed.
    /*
    Scene += new Scene.ImageNode("bg uni.jpg") {
      pose.position.z = -10.0f
    }
    Scene += new Scene.ImageNode("sylvie blue normal.png") {
      pose.position.y = -10.0f
    }
    Scene += new Scene.ImageNode("sylvie blue smile.png") {
      pose.position.z = 5.0f
    }

    Scene += new Scene.ImageNode("textbox.png") {
      pose.position.z = 10.0f
      pose.position.y = -268.0f
    }


        val node = new Scene.TextNode("")
        node.maxWidth = 700.0f / 0.3f
        node.pose.position.z = 20.0f
        node.pose.position.y = -220.0f
        node.pose.position.x = -360.0f
        node.pose.scale.x = 0.3f
        node.pose.scale.y = 0.3f
        Scene += node


        Scene += new Scene.TextNode("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse euismod diam nec elit ultrices rhoncus. Nullam non facilisis est. Morbi tempor facilisis aliquet. Donec eu feugiat sapien.") {
          maxWidth = 700.0f / 0.3f
          pose.position.z = 20.0f
          pose.position.y = -220.0f
          pose.position.x = -360.0f
          pose.scale.x = 0.3f
          pose.scale.y = 0.3f
        }
    */

    for {
      _ <- scene("bg uni.jpg")
      _ <- say("Veni Vidi Vici")
      _ <- say ("Hello World!")
      _ <- scene("bg meadow.jpg")
      _ <- say ("In vino veritas")
      _ <- say ("Hello World!")
      _ <- Events.waitForKeyPress(KeyEscape)
    } yield()
  }



}
