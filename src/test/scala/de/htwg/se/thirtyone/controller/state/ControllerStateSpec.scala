package de.htwg.se.thirtyone.controller.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.controller.GameController

class ControllerStateSpec extends AnyWordSpec with Matchers {
  "ControllerState.checkIfGameEnded" should {
    "set state to GameEndedState and notify GameEnded when gameRunning is false" in {
      val events = ArrayBuffer.empty[String]
      val p1 = Player(playersHealth = 1)
      val p2 = Player(playersHealth = 1)
      val gd = GameData(2).copy(gameRunning = false, players = List(p1, p2))
      val controller = new GameController(PlayingState, gd)
      controller.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })

      val stub = new ControllerState {
        override def execute(input: String, c: ControllerInterface): Unit = ()
      }

      val player = 1
      stub.checkIfRoundEnded(controller, player)

      controller.state shouldBe GameEndedState
      events.exists(_.contains(s"GameEnded")) shouldBe true
    }
  }

  "ControllerState.handleInput" should {
    "handle quit/exit" in {
      val controller = new GameController(PlayingState, GameData(2))
      var executed = false
      val stub = new ControllerState {
        override def execute(input: String, c: ControllerInterface): Unit = {
          executed = true
        }
      }
      
      stub.handleInput("something", controller)
      executed shouldBe true
    }
  }
}
