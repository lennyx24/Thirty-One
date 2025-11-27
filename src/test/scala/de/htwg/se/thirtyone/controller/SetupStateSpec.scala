package de.htwg.se.thirtyone.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.GameData
import de.htwg.se.thirtyone.util._
import scala.collection.mutable.ArrayBuffer

class SetupStateSpec extends AnyWordSpec with Matchers {
  "SetupState" should {

    val events = ArrayBuffer.empty[String]
    def makeController(): GameController =
      new GameController(SetupState, GameData(2)) {
        override def notifyObservers(event: GameEvent): Unit = events += event.toString
      }

    "initialize on valid player count" in {
      events.clear()
      val controller = makeController()

      SetupState.execute("3", controller)
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
