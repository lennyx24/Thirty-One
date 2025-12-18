package de.htwg.se.thirtyone.aview

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._

class ConsoleViewSpec extends AnyWordSpec with Matchers {
  "ConsoleView" should {
    "print something on GameStarted" in {
      val controller = new GameController(de.htwg.se.thirtyone.controller.state.SetupState, GameData(2))
      val view = new ConsoleView(controller)
      val baos = new java.io.ByteArrayOutputStream()
      val ps = new java.io.PrintStream(baos)
      scala.Console.withOut(ps) {
        view.update(GameStarted)
      }
      val out = baos.toString
      out should not be empty
      // expect at least the prompt about number of players or similar
      (out.contains("Spieler") || out.toLowerCase.contains("wie viele") || out.toLowerCase.contains("willkommen")) shouldBe true
    }

    "print player score when PlayerScore notification is received" in {
      val controller = new GameController(de.htwg.se.thirtyone.controller.state.PlayingState, GameData(2))
      // ensure known points for player 1
      controller.gameData = controller.gameData.calculatePlayerPoints(1)
      val view = new ConsoleView(controller)
      val baos = new java.io.ByteArrayOutputStream()
      val ps = new java.io.PrintStream(baos)
      scala.Console.withOut(ps) {
        view.update(PlayerScore(1))
      }
      val out = baos.toString
      out should not be empty
      (out.contains("Punkte") || out.toLowerCase.contains("spieler")) shouldBe true
    }

    "print table when printNewRound is called" in {
      val controller = new GameController(de.htwg.se.thirtyone.controller.state.PlayingState, GameData(2))
      val view = new ConsoleView(controller)
      // create a small table and capture output
      val table = Table()
      val baos = new java.io.ByteArrayOutputStream()
      val ps = new java.io.PrintStream(baos)
      scala.Console.withOut(ps) {
        view.printNewRound(table)
      }
      val out = baos.toString
      out should not be empty
    }
  }
}

