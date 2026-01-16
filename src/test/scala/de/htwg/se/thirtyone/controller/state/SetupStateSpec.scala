package de.htwg.se.thirtyone.controller.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.gameImplementation.GameData
import de.htwg.se.thirtyone.util._
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
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
  }
}
