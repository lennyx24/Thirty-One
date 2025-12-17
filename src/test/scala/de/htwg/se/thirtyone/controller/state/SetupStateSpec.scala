package de.htwg.se.thirtyone.controller.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.GameData
import de.htwg.se.thirtyone.util._
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller.GameController

class SetupStateSpec extends AnyWordSpec with Matchers {
  "SetupState" should {

    val events = ArrayBuffer.empty[String]
    def makeController(): GameController = {
      val c = new GameController(SetupState, GameData(2))
      c.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })
      c
    }

    "initialize on valid player count" in {
      events.clear()
      val controller = makeController()

      SetupState.selectNumber("3", controller)
      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(_.contains("RunningGame(")) shouldBe true
      controller.state shouldBe PlayingState
    }

    "invalid input" in {
      events.clear()
      val controller = makeController()
      SetupState.execute("9", controller)
      events.exists(_.contains("InvalidInput")) shouldBe true
    }
  }
}

