package de.htwg.se.thirtyone.controller.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.game.GameData
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.implementation.GameController
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
      events.clear()
      SetupState.execute("2", controller)
      events.exists(_.contains("PlayerName(1)")) shouldBe true
    }

    "notify InvalidInput on other input" in {
      val events = ArrayBuffer.empty[String]
      val controller = new GameController(GameEndedState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })

      GameEndedState.execute("x", controller)
      events.exists(_.contains("InvalidInput")) shouldBe true
    }

    "call System.exit on 'n'" in {
      val controller = new GameController(GameEndedState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val original = ExitHandler.exit
      try {
        ExitHandler.exit = _ => throw new SecurityException("exit")
        an [SecurityException] should be thrownBy GameEndedState.execute("n", controller)
      } finally {
        ExitHandler.exit = original
      }
    }
  }
}
