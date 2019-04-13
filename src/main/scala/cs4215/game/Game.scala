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
  def run(): Future[Unit] =
    scene("bg uni") |>
    "When we come out of the university, I spot her right away." |>
    show("sylvie green normal") |>
    "Sylvie's got a big heart and she's always been a good friend to me." |>
    menu("As soon as she catches my eye, I decide...",
      ("To ask her right away.", rightaway),
      ("To ask her later.", later))

  def rightaway(): Future[Unit] =
    show("sylvie green smile") |>
    say(m, "Are you going home now? Wanna walk back with me?") |>
    say(s, "Why not?") |>
    sceneBlack() |>
    "Good Ending."

  def later(): Future[Unit] =
    "I can't get up the nerve to ask right now. With a gulp, I decide to ask her later." |>
    sceneBlack() |>
    "Bad Ending."
}
