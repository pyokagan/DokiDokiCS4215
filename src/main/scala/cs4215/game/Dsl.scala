package cs4215.game

import cs4215.events._
import cs4215.scene._

import scala.concurrent.{ExecutionContext, Future}

object Dsl {
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(Events.enqueueCallback)
  case class Character(name: String = "", color: String = "#ffffff")

  implicit class FutureWithDsl[T](a: Future[T]) {
    def |>[S](b: => Future[S]): Future[S] =
      a.flatMap(_ => b)

    def |>(b: String): Future[Unit] =
      a.flatMap(_ => say(b))
  }

  implicit class StringWithDsl(a: String) {
    def |>[S](b: => Future[S]): Future[S] =
      say(a).flatMap(_ => b)

    def |>(b: String): Future[Unit] =
      say(a).flatMap(_ => say(b))
  }

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

  def fadeIn(node: Scene.SceneNode, speed: Float): Future[Unit] = {
    def aux(left: Float): Future[Unit] = {
      node.opacity = left
      if (left >= 1f) Events.nextEvent()
      else Events.waitForTick().flatMap(_ => aux(left + speed))
    }
    aux(0f)
  }

  def say(char: Character, msg: String): Future[Unit] = {
    for {
      _ <- {
        Scene += textbox
        Scene += charName
        Scene += msgNode
        msgNode.text = msg
        charName.text = char.name
        fadeIn(msgNode, 0.15f)
      }
      _ <- Events.waitForMouseButtonPress(MouseButtonLeft)
      _ <- {
        Scene -= charName
        Scene -= msgNode
        Scene -= textbox
        Events.nextEvent
      }
    } yield()
  }

  def say(charName: String, msg: String): Future[Unit] = {
    say(new Character(charName), msg)
  }

  def say(msg: String): Future[Unit] = {
    say(new Character(), msg)
  }

  def scene(texture: Texture): Future[Unit] = {
    val bg = new Scene.ImageNode(texture)
    bg.pose.position.z = -100.0f
    Scene.clear()
    Scene += bg
    Events.nextEvent()
  }

  def sceneBlack(): Future[Unit] = {
    Scene.clear()
    Events.nextEvent()
  }

  def show(texture: Texture): Future[Unit] = {
    val node = new Scene.SpriteNode(texture)
    for {
      _ <- {
        Scene.clearSprites()
        Scene += node
        Events.nextEvent
      }
    } yield()
  }

  def menu(msg: String, choices: (String, () => Future[Unit])*): Future[Unit] = {
    val BgImageHeight = 60 // with some padding
    val totalHeight = choices.length * BgImageHeight
    val bgNodes = (0 until choices.length).map(i => {
      val p = new Scene.ImageNode("choice_idle_background")
      p.pose.position.y = totalHeight / 2f - i * BgImageHeight
      p.pose.position.z = 30f
      p
    })
    val textNodes = choices.zipWithIndex.map { case ((text, _), i) =>
      val p = new Scene.TextNode(text)
      p.pose.position.x = -280f
      p.pose.position.y = totalHeight / 2f - i * BgImageHeight - 8f
      p.pose.position.z = 35f
      p.pose.scale.set(0.25f, 0.25f, 1f)
      p
    }
    val boxes = choices.zipWithIndex.map { case ((_, f), i) =>
      val ycenter = totalHeight / 2f - i * BgImageHeight
      val xmin = -280f
      val ymin = ycenter - 17f
      val xmax = 280f
      val ymax = ycenter + 17f
      (f, xmin, ymin, xmax, ymax)
    }
    val allNodes = bgNodes ++ textNodes
    Scene ++= allNodes
    def aux(): Future[Unit] = {
      Events.waitForMouseButtonPress(MouseButtonLeft).flatMap(_ => {
        boxes.find { case (_, xmin, ymin, xmax, ymax) =>
          val xpos = Events.cursorXpos
          val ypos = Events.cursorYpos
          xpos >= xmin && xpos <= xmax && ypos >= ymin && ypos <= ymax
        }.map { case (f, _, _, _, _) => Scene --= allNodes; f() }.getOrElse(aux())
      })
    }
    Scene += textbox
    Scene += msgNode
    msgNode.text = msg
    fadeIn(msgNode, 0.15f) |> aux()
  }
}
