package de.htwg.se.thirtyone.controller.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.gameImplementation.GameData
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.controller.command.UndoManager
import scala.collection.mutable.ArrayBuffer

class GameEndedStateSpec extends AnyWordSpec with Matchers {
  "GameEndedState" should {
    "transition to SetupState on 'j'" in {
      val events = ArrayBuffer.empty[String]
      val controller = new GameController(GameEndedState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })

      GameEndedState.execute("j", controller)
      controller.state shouldBe SetupState
      events.exists(_.contains("GameStarted")) shouldBe true
      // Also SetupState.reset() should have been called (playerAmount=0)
      SetupState.playerAmount shouldBe 0
    }

    "notify InvalidInput on other input" in {
      val events = ArrayBuffer.empty[String]
      val controller = new GameController(GameEndedState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })

      GameEndedState.execute("x", controller)
      events.exists(_.contains("InvalidInput")) shouldBe true
    }
  }
}