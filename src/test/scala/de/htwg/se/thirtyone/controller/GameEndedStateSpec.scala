package de.htwg.se.thirtyone.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.GameData
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.state._
import scala.collection.mutable.ArrayBuffer

class GameEndedStateSpec extends AnyWordSpec with Matchers {
  "GameEndedState" should {

    val events = ArrayBuffer.empty[String]
    def makeController(): GameController =
      new GameController(GameEndedState, GameData(2)) {
        override def notifyObservers(event: GameEvent): Unit = events += event.toString
      }

    "restart on 'j'" in {
      events.clear()
      val controller = makeController()

      GameEndedState.execute("j", controller)
      controller.state shouldBe SetupState
      events.exists(_.contains("GameStarted")) shouldBe true
    }

    "invalid input" in {
      events.clear()
      val controller = makeController()

      GameEndedState.execute("x", controller)
      events.exists(_.contains("InvalidInput")) shouldBe true
    }
  }
}
