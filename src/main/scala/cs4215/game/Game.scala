package cs4215.game

import cs4215.events._
import cs4215.game.Dsl._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Main entry point for the game.
 */
object Game {
  // Declare characters used by this game.
  val s = new Character("Sylvie", color = "#abcdef")
  val m = new Character("Me", color = "#c8c8ff")

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
