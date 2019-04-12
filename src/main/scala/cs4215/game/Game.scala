package cs4215.game

import cs4215.game.Dsl._

import scala.concurrent.Future

/**
 * Main entry point for the game.
 */
object Game {
  // Declare characters used by this game.
  val s = new Character("Sylvie", color = "#abcdef")
  val m = new Character("Me", color = "#c8c8ff")

  // The game starts here.
  def run(): Future[Unit] = for {
    _ <- scene("bg uni")
    _ <- say("When we come out of the university, I spot her right away.")
    _ <- show("sylvie green normal")
    _ <- say("Sylvie's got a big heart and she's always been a good friend to me.")
    _ <- menu("As soon as she catches my eye, I decide...",
      ("To ask her right away.", rightaway),
      ("To ask her later.", later))
  } yield ()

  def rightaway(): Future[Unit] = for {
    _ <- show("sylvie green smile")
    _ <- say(m, "Are you going home now? Wanna walk back with me?")
    _ <- say(s, "Why not?")
    _ <- sceneBlack()
    _ <- say("Good Ending.")
  } yield ()

  def later(): Future[Unit] = for {
    _ <- say("I can't get up the nerve to ask right now. With a gulp, I decide to ask her later.")
    _ <- sceneBlack()
    _ <- say("Bad Ending.")
  } yield ()
}
