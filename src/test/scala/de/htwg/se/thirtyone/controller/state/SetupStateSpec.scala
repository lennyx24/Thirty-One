package de.htwg.se.thirtyone.controller.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.game.GameData
import de.htwg.se.thirtyone.util._
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller.implementation.GameController
import de.htwg.se.thirtyone.controller.command.UndoManager

class SetupStateSpec extends AnyWordSpec with Matchers {
  "SetupState" should {

    val events = ArrayBuffer.empty[String]
    def makeController(): GameController = {
      val c = new GameController(SetupState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      c.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })
      c
    }

    "initialize on valid player count" in {
      events.clear()
      SetupState.reset()
      val controller = makeController()

      SetupState.selectNumber("3", controller)
      events.exists(_.contains("PlayerName(1)")) shouldBe true
    }

    "prompt names and start game after all names are entered" in {
      events.clear()
      SetupState.reset()
      val controller = makeController()

      SetupState.selectNumber("3", controller)
      events.clear()

      SetupState.execute("Alice", controller)
      SetupState.execute("Bob", controller)
      SetupState.execute("Cara", controller)

      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(_.contains("RunningGame(")) shouldBe true
      controller.state shouldBe PlayingState
    }

    "notify InvalidInput when more names are provided than player count" in {
      events.clear()
      SetupState.reset()
      val controller = makeController()

      SetupState.selectNumber("2", controller)
      SetupState.execute("Alice", controller)
      SetupState.execute("Bob", controller)
      events.clear()

      SetupState.execute("Cara", controller)
      events.exists(_.contains("InvalidInput")) shouldBe true
    }

    "handle empty name input by assigning default name" in {
      events.clear()
      SetupState.reset()
      val controller = makeController()
      SetupState.selectNumber("2", controller)
      
      SetupState.execute("  ", controller)
      events.exists(_.contains("PlayerNameSet(1,Player 1)")) shouldBe true
    }

    "handle invalid player count input" in {
      events.clear()
      SetupState.reset()
      val controller = makeController()
      
      SetupState.selectNumber("1", controller)
      events.exists(_.contains("InvalidInput")) shouldBe true
      
      SetupState.selectNumber("5", controller)
      events.exists(_.contains("InvalidInput")) shouldBe true
      
      SetupState.selectNumber("abc", controller)
       events.exists(_.contains("InvalidInput")) shouldBe true
    }

    "setupGame should start immediately with provided names" in {
      events.clear()
      SetupState.reset()
      val controller = makeController()

      SetupState.setupGame(2, List("Alice", "Bob"), controller)

      controller.state shouldBe PlayingState
      controller.gameData.players.head.name shouldBe "Alice"
      events.exists(_.contains("RunningGame(")) shouldBe true
    }

    "setupGame should reject invalid player count" in {
      events.clear()
      SetupState.reset()
      val controller = makeController()

      SetupState.setupGame(1, List("A"), controller)
      events.exists(_.contains("InvalidInput")) shouldBe true
    }
  }
}
