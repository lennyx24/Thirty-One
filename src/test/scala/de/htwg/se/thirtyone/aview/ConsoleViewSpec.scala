package de.htwg.se.thirtyone.aview

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.implementation.GameController
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.model.game.{GameData, Player, Table}
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.util._

class ConsoleViewSpec extends AnyWordSpec with Matchers {
  private def captureOut(f: => Unit): String = {
    val baos = new java.io.ByteArrayOutputStream()
    val ps = new java.io.PrintStream(baos)
    scala.Console.withOut(ps)(f)
    baos.toString
  }

  private def info(player: Player): PlayerInfo =
    PlayerInfo(player.id, player.name)

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
      val out = captureOut { view.update(PlayerScore(info(scorePlayer))) }
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
      val passed = captureOut { view.update(PlayerPassed(info(player1))) }
      passed should include(player1.name)

      val knocked = captureOut { view.update(PlayerKnocked(info(player2))) }
      knocked should include("hat diese Runde geklopft")

      val swapped = captureOut { view.update(PlayerSwapped(info(player1))) }
      swapped should include("tauscht")
    }

    "print PlayerNameSet message" in {
      val controller = new GameController(SetupState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val view = ConsoleView(controller)
      val out = captureOut { view.update(PlayerNameSet(1, "Lenny")) }
      out should include("Spieler 1 heißt nun Lenny")
    }

    "print InvalidInput message" in {
      val controller = new GameController(PlayingState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val view = ConsoleView(controller)
      val out = captureOut { view.update(InvalidInput) }
      out should include("keine valide Option")
    }

    "print RunningGame message" in {
      val controller = new GameController(PlayingState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val view = ConsoleView(controller)
      val out = captureOut { view.update(RunningGame(info(controller.gameData.players.head))) }
      out should include("ist dran")
    }

    "print PlayerSwapGive message" in {
      val controller = new GameController(PlayingState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val view = ConsoleView(controller)
      val out = captureOut { view.update(PlayerSwapGive(info(controller.gameData.players.head))) }
      out should include("abgeben")
    }

    "print PlayerSwapTake message" in {
      val controller = new GameController(PlayingState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val view = ConsoleView(controller)
      val out = captureOut { view.update(PlayerSwapTake(info(controller.gameData.players.head))) }
      out should include("erhalten")
    }

    "print GameEnded message" in {
      val controller = new GameController(PlayingState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val view = ConsoleView(controller)
      val out = captureOut { view.update(GameEnded(info(controller.gameData.players.head))) }
      out should include("gewonnen")
    }

    "print PlayerName message" in {
      val controller = new GameController(SetupState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val view = ConsoleView(controller)
      val out = captureOut { view.update(PlayerName(1)) }
      out should include("Name für Spieler")
    }
  }
}
