package de.htwg.se.thirtyone.aview

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.model.gameImplementation.{GameData, Table}
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.util._

class ConsoleViewSpec extends AnyWordSpec with Matchers {
  // helper für Konsole-Capture
  private def captureOut(f: => Unit): String = {
    val baos = new java.io.ByteArrayOutputStream()
    val ps = new java.io.PrintStream(baos)
    scala.Console.withOut(ps)(f)
    baos.toString
  }

  "ConsoleView" should {
    "print something on GameStarted" in {
      val controller = new GameController(SetupState, GameData(2), new UndoManager())
      val view = ConsoleView(controller)
      val out = captureOut { view.update(GameStarted) }
      out should not be empty
      (out.contains("Spieler") || out.toLowerCase.contains("wie viele") || out.toLowerCase.contains("willkommen")) shouldBe true
    }

    "print player score when PlayerScore notification is received" in {
      val controller = new GameController(PlayingState, GameData(2), new UndoManager())
      controller.setGameData(controller.gameData.calculatePlayerPoints(1))
      val view = ConsoleView(controller)
      val out = captureOut { view.update(PlayerScore(1)) }
      out should not be empty
      (out.contains("Punkte") || out.toLowerCase.contains("spieler")) shouldBe true
    }

    "print table when printNewRound is called" in {
      val controller = new GameController(PlayingState, GameData(2), new UndoManager())
      val view = ConsoleView(controller)
      val table = Table()
      val out = captureOut { view.printNewRound(table.printTable(controller.gameData.players)) }
      out should not be empty
    }
  }
}
