package de.htwg.se.thirtyone.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.gameImplementation.GameData
import de.htwg.se.thirtyone.util._
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.controller.command.UndoManager

class SwapStateSpec extends AnyWordSpec with Matchers {
  "SwapState" should {
    val events = ArrayBuffer.empty[String]
    def makeController(state: SwapState = new SwapState): GameController = {
      val c = new GameController(state, GameData(2), new UndoManager())
      c.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })
      c
    }

    "handle give then take swap" in {
      events.clear()
      val state = new SwapState
      val controller = makeController(state)

      state.selectNumber("1", controller)
      events.exists(_.contains("PlayerSwapTake(1)")) shouldBe true

      events.clear()
      state.selectNumber("1", controller)

      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(_.contains("PlayerSwapped(1)")) shouldBe true
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

      state.selectAll(controller)

      events.exists(_.contains("PlayerSwapped(")) shouldBe true
      controller.state shouldBe PlayingState
    }

    "when give already set and input is 'alle' -> InvalidInput" in {
      events.clear()
      val state = new SwapState
      val controller = makeController(state)

      state.give = "1"
      state.selectAll(controller)

      events.exists(_.contains("InvalidInput")) shouldBe true
      controller.state shouldBe state
    }
  }
}
