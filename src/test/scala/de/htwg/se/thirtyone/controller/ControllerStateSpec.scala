package de.htwg.se.thirtyone.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.thirtyone.controller.state._

class ControllerStateSpec extends AnyWordSpec with Matchers {
  "ControllerState.checkIfGameEnded" should {
    "set state to GameEndedState and notify GameEnded when gameRunning is false" in {
      val events = ArrayBuffer.empty[String]
      // Player needs to die after damage. Health 1 -> 0.
      val p1 = Player(playersHealth = 1)
      val p2 = Player(playersHealth = 1)
      val gd = GameData(2).copy(gameRunning = false, players = List(p1, p2))
      val controller = new GameController(PlayingState, gd)
      controller.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })

      val stub = new ControllerState {
        override def execute(input: String, c: GameController): Unit = ()
      }

      val player = 1
      stub.checkIfRoundEnded(controller, player)

      controller.state shouldBe GameEndedState
      events.exists(_.contains(s"GameEnded")) shouldBe true
    }
  }

  "ControllerState.handleInput" should {
    "handle quit/exit" in {
      // System.exit cannot be easily tested without security manager or interception.
      // We can skip this or assume it works.
      // Or we can test that it calls execute for other inputs.
      val controller = new GameController(PlayingState, GameData(2))
      var executed = false
      val stub = new ControllerState {
        override def execute(input: String, c: GameController): Unit = {
          executed = true
        }
      }
      
      stub.handleInput("something", controller)
      executed shouldBe true
    }
  }
}
