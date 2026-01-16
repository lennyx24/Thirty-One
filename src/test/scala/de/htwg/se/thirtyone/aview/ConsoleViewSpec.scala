package de.htwg.se.thirtyone.aview

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.model.gameImplementation.{GameData, Player, Table}
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.util._

class ConsoleViewSpec extends AnyWordSpec with Matchers {
  // helper für Konsole-Capture (lokal)
  private def captureOut(f: => Unit): String = {
    val baos = new java.io.ByteArrayOutputStream()
    val ps = new java.io.PrintStream(baos)
    scala.Console.withOut(ps)(f)
    baos.toString
  }

  "ConsoleView" should {
    "print something on GameStarted" in {
      val controller = new GameController(SetupState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val view = ConsoleView(controller)
      val out = captureOut { view.update(GameStarted) }
      out should not be empty
      (out.contains("Spieler") || out.toLowerCase.contains("wie viele") || out.toLowerCase.contains("willkommen")) shouldBe true
    }

    "print player score when PlayerScore notification is received" in {
      val base = GameData(2)
      val custom = base.copy(players = List(Player(name = "Alice"), Player(name = "Bob")))
      val controller = new GameController(PlayingState, custom, new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.setGameData(controller.gameData.calculatePlayerPoints(controller.gameData.players(0)))
      val view = ConsoleView(controller)
      val scorePlayer = controller.gameData.players(1)
      val out = captureOut { view.update(PlayerScore(scorePlayer)) }
      out should not be empty
      (out.contains("Punkte") || out.toLowerCase.contains("spieler")) shouldBe true
    }

    "print table when PrintTable event is received" in {
      val controller = new GameController(PlayingState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val view = ConsoleView(controller)
      val out = captureOut { view.update(PrintTable) }
      out should not be empty
      out should include("+")
    }

    "print PlayerPassed/Knocked/Swapped messages" in {
      val base = GameData(2)
      val custom = base.copy(players = List(Player(name = "Alice"), Player(name = "Bob")))
      val controller = new GameController(PlayingState, custom, new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val view = ConsoleView(controller)
      val player1 = controller.gameData.players.head
      val player2 = controller.gameData.players(1)
      val passed = captureOut { view.update(PlayerPassed(player1)) }
      passed should include(player1.name)

      val knocked = captureOut { view.update(PlayerKnocked(player2)) }
      knocked should include("hat diese Runde geklopft")

      val swapped = captureOut { view.update(PlayerSwapped(player1)) }
      swapped should include("tauscht")
    }
  }
}
