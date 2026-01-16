package de.htwg.se.thirtyone.controller.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.gameImplementation.{GameData, Player}
import de.htwg.se.thirtyone.util._

import scala.collection.mutable.ArrayBuffer
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.controller.command.UndoManager

class PlayingStateSpec extends AnyWordSpec with Matchers {
  "PlayingState" should {

    val events = ArrayBuffer.empty[String]

    def stubGameData(
                      index: Int = 0
                    ): GameData =
      val base = GameData(3)
      val players = List(
        Player(name = "P1", hasKnocked = false, points = 15.0, playersHealth = 3),
        Player(name = "P2", hasKnocked = false, points = 10.0, playersHealth = 2),
        Player(name = "P3", hasKnocked = false, points = 25.0, playersHealth = 1)
      )
      base.copy(players = players, currentPlayerIndex = index)

    def makeController(gd: GameData): GameController = {
      val c = new GameController(PlayingState, gd, new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      c.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })
      c
    }

    "execute passen" in {
      events.clear()
      val controller = makeController(stubGameData())
      val beforeIndex = controller.gameData.currentPlayerIndex

      controller.subscribers.size should be > 0
      controller.notifyObservers(PrintTable)
      events.exists(_.contains("PrintTable")) shouldBe true
      events.clear()

      val beforeHasPassed = controller.gameData.players(beforeIndex).hasPassed

      PlayingState.pass(controller)

      val afterHasPassed = controller.gameData.players(beforeIndex).hasPassed
      afterHasPassed shouldBe true
      val actedPlayer = controller.gameData.players(beforeIndex)

      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(_.contains(s"PlayerPassed($actedPlayer)")) shouldBe true
      events.exists(_.contains("RunningGame")) shouldBe true
    }

    "execute klopfen" in {
      events.clear()
      val controller = makeController(stubGameData())
      val beforeIndex = controller.gameData.currentPlayerIndex

      PlayingState.knock(controller)
      val actedPlayer = controller.gameData.players(beforeIndex)

      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(_.contains(s"PlayerKnocked($actedPlayer)")) shouldBe true
      events.exists(_.contains("RunningGame")) shouldBe true
    }

    "execute tauschen" in {
      events.clear()
      val controller = makeController(stubGameData())
      val currentPlayer = controller.gameData.currentPlayer

      PlayingState.swap(controller)

      controller.state should not be PlayingState
      events.exists(_.contains(s"PlayerSwapGive($currentPlayer)")) shouldBe true
    }

    "skip pass notifications when round ends" in {
      events.clear()
      val p1 = Player(name = "P1", points = 5.0, playersHealth = 1)
      val p2 = Player(name = "P2", points = 10.0, playersHealth = 2)
      val gd = GameData(2).copy(players = List(p1, p2), gameRunning = false)
      val controller = makeController(gd)

      PlayingState.pass(controller)

      events.exists(_.contains("GameEnded")) shouldBe true
      events.exists(_.contains("PlayerPassed")) shouldBe false
      controller.state shouldBe GameEndedState
    }

    "handle invalid input" in {
      events.clear()
      val controller = makeController(stubGameData())

      PlayingState.execute("unbekannt", controller)

      events.exists(_.contains("InvalidInput")) shouldBe true
    }
  }
}
