package de.htwg.se.thirtyone.controller.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.gameImplementation.{GameData, Table}
import de.htwg.se.thirtyone.util._
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.controller.command.UndoManager

class SwapStateSpec extends AnyWordSpec with Matchers {
  "SwapState" should {
    val events = ArrayBuffer.empty[String]
    def makeController(state: SwapState = new SwapState): GameController = {
      val c = new GameController(state, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      c.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })
      c
    }

    "handle give then take swap" in {
      events.clear()
      val state = new SwapState
      val controller = makeController(state)
      val currentPlayer = controller.gameData.currentPlayer

      state.execute("1", controller)
      events.exists(e => e.contains("PlayerSwapTake(") && e.contains(currentPlayer.name)) shouldBe true

      events.clear()
      state.execute("1", controller)

      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(e => e.contains("PlayerSwapped(") && e.contains(currentPlayer.name)) shouldBe true
      events.exists(_.contains("RunningGame(")) shouldBe true
      controller.state shouldBe PlayingState
    }

    "handle invalid input" in {
      events.clear()
      val controller = makeController(new SwapState)

      controller.state.asInstanceOf[SwapState].execute("x", controller)
      events.exists(_.contains("InvalidInput")) shouldBe true
    }

    "handle 'alle' then take (all cards)" in {
      events.clear()
      val state = new SwapState
      val controller = makeController(state)
      val currentPlayer = controller.gameData.currentPlayer

      state.execute("alle", controller)

      events.exists(e => e.contains("PlayerSwapped(") && e.contains(currentPlayer.name)) shouldBe true
      controller.state shouldBe PlayingState
    }

    "notify InvalidInput when swap fails after valid inputs" in {
      events.clear()
      val state = new SwapState
      val controller = makeController(state)
      controller.setGameData(controller.gameData.asInstanceOf[GameData].copy(table = Table()))

      state.execute("1", controller)
      events.clear()
      state.execute("1", controller)

      events.exists(_.contains("InvalidInput")) shouldBe true
    }

    "when give already set and input is 'alle' -> InvalidInput" in {
      events.clear()
      val state = new SwapState
      val controller = makeController(state)

      state.give = "1"
      state.execute("alle", controller)

      events.exists(_.contains("InvalidInput")) shouldBe true
      controller.state shouldBe a[ControllerState]
    }
  }
}
