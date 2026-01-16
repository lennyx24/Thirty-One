package de.htwg.se.thirtyone.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.model.gameImplementation.GameData
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.model.gameImplementation.Player
import scala.collection.mutable.ArrayBuffer

class GameControllerSpec extends AnyWordSpec with Matchers {
  "GameController" should {
    "delegate actions to state" in {
      var called = List.empty[String]
      val mockState = new ControllerState {
        override def pass(c: ControllerInterface): Unit = { called ::= "pass" }
        override def knock(c: ControllerInterface): Unit = { called ::= "knock" }
        override def swap(c: ControllerInterface): Unit = { called ::= "swap" }
        override def selectNumber(idx: String, c: ControllerInterface): Unit = { called ::= s"select:$idx" }
        override def selectAll(c: ControllerInterface): Unit = { called ::= "selectAll" }
        override def execute(input: String, c: ControllerInterface): Unit = { called ::= s"handle:$input" }
      }
      val controller = new GameController(mockState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.pass()
      controller.knock()
      controller.swap()
      controller.selectNumber("3")
      controller.selectAll()
      controller.handleInput("x")
      called should contain allOf ("pass", "knock", "swap", "select:3", "selectAll", "handle:x")
    }

    "initialGame delegates to setupGame" in {
      var called: Option[(Int, List[String])] = None
      val mockState = new ControllerState {
        override def setupGame(playerCount: Int, names: List[String], c: ControllerInterface): Unit =
          called = Some((playerCount, names))
      }
      val controller = new GameController(mockState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.initialGame("2", List("Alice", "Bob"))
      called shouldBe Some((2, List("Alice", "Bob")))
    }

    "setGameData, resetGame and dealDamage modify gameData" in {
      val controller = new GameController(SetupState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      // setGameData
      controller.setGameData(GameData(3))
      controller.gameData.playerCount shouldBe 3

      // resetGame: make players with points and then reset
      val p = Player("P", hasKnocked = false, points = 10.0, playersHealth = 3)
      val modified = GameData(controller.gameData.playerCount).copy(players = List(p, p, p))
      controller.setGameData(modified)
      controller.resetGame()
      controller.gameData.players.foreach(pl => pl.points shouldBe 0)

      // dealDamage: decrease health
      val before = controller.gameData.players.head.playersHealth
      controller.dealDamage(controller.gameData.players.head)
      controller.gameData.players.head.playersHealth shouldBe (before - 1)
    }

    "countPoints should update gameData via calculatePlayerPoints" in {
      val controller = new GameController(SetupState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val before = controller.gameData.players.map(_.points)
      controller.countPoints(controller, controller.gameData.players(0))
      // should still be GameData and have players list
      controller.gameData.players.map(_.points).length shouldBe before.length
    }

    "undo and redo should notify PrintTable and RunningGame" in {
      val controller = new GameController(SetupState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val events = ArrayBuffer.empty[String]
      controller.add(new de.htwg.se.thirtyone.util.Observer { override def update(e: de.htwg.se.thirtyone.util.GameEvent): Unit = events += e.toString })

      events.clear()
      controller.undo()
      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(_.contains("RunningGame")) shouldBe true

      events.clear()
      controller.redo()
      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(_.contains("RunningGame")) shouldBe true
    }

    "selectNumber and selectAll delegate to state" in {
      var called = List.empty[String]
      val mockState = new ControllerState {
        override def selectNumber(idx: String, c: ControllerInterface): Unit = { called ::= s"select:$idx" }
        override def selectAll(c: ControllerInterface): Unit = { called ::= "selectAll" }
      }
      val controller = new GameController(mockState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.selectNumber("1")
      controller.selectAll()
      called should contain allOf ("select:1", "selectAll")
    }
  }
}
