package de.htwg.se.thirtyone.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.thirtyone.model.GameData
import de.htwg.se.thirtyone.util._
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.thirtyone.controller.state._

class ControllerStateSpec extends AnyWordSpec with Matchers {
  "ControllerState.checkIfGameEnded" should {
    "set state to GameEndedState and notify GameEnded when gameRunning is false" in {
      val events = ArrayBuffer.empty[String]
      val gd = GameData(2).copy(gameRunning = false)
      val controller = new GameController(PlayingState, gd)
      controller.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })

      val stub = new ControllerState {
        override def execute(input: String, c: GameController): Unit = ()
      }

      val player = 1
      stub.checkIfRoundEnded(controller, player)

      controller.state shouldBe GameEndedState
      events.exists(_.contains(s"GameEnded($player)")) shouldBe true
    }
  }
}
