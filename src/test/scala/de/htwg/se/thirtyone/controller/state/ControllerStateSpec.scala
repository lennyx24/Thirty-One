package de.htwg.se.thirtyone.controller.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.thirtyone.model.gameImplementation.{Player, GameData}
import de.htwg.se.thirtyone.util._
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.controller.command.UndoManager
import java.security.Permission

class ControllerStateSpec extends AnyWordSpec with Matchers {
  "ControllerState.checkIfGameEnded" should {
    "set state to GameEndedState and notify GameEnded when gameRunning is false" in {
      val events = ArrayBuffer.empty[String]
      val p1 = Player(playersHealth = 1)
      val p2 = Player(playersHealth = 1)
      val gd = GameData(2).copy(gameRunning = false, players = List(p1, p2))
      val controller = new GameController(PlayingState, gd, new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })

      val stub = new ControllerState {
        override def execute(input: String, c: ControllerInterface): Unit = ()
      }

      stub.checkIfRoundEnded(controller, p1)

      controller.state shouldBe GameEndedState
      events.exists(_.contains(s"GameEnded")) shouldBe true
    }
  }

  "ControllerState.handleInput" should {
    "delegate non-quit input to execute" in {
      val controller = new GameController(PlayingState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      var executed = false
      val stub = new ControllerState {
        override def execute(input: String, c: ControllerInterface): Unit = {
          executed = true
        }
      }
      
      stub.handleInput("something", controller)
      executed shouldBe true
    }

    "call System.exit on quit" in {
      class NoExitSecurityManager extends SecurityManager {
        override def checkPermission(perm: Permission): Unit = ()
        override def checkPermission(perm: Permission, context: Object): Unit = ()
        override def checkExit(status: Int): Unit = throw new SecurityException(status.toString)
      }

      val controller = new GameController(PlayingState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      var executed = false
      val stub = new ControllerState {
        override def execute(input: String, c: ControllerInterface): Unit = {
          executed = true
        }
      }

      val original = System.getSecurityManager
      try {
        System.setSecurityManager(new NoExitSecurityManager)
        an [SecurityException] should be thrownBy stub.handleInput("quit", controller)
        executed shouldBe false
      } finally {
        System.setSecurityManager(original)
      }
    }

    "default methods notify InvalidInput" in {
      val controller = new GameController(PlayingState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val events = ArrayBuffer.empty[String]
      controller.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })

      val stub = new ControllerState {}
      events.clear()
      stub.execute("x", controller)
      events.exists(_.contains("InvalidInput")) shouldBe true

      events.clear()
      stub.pass(controller)
      events.exists(_.contains("InvalidInput")) shouldBe true

      events.clear()
      stub.knock(controller)
      events.exists(_.contains("InvalidInput")) shouldBe true

      events.clear()
      stub.swap(controller)
      events.exists(_.contains("InvalidInput")) shouldBe true

      events.clear()
      stub.selectNumber("1", controller)
      events.exists(_.contains("InvalidInput")) shouldBe true

      events.clear()
      stub.selectAll(controller)
      events.exists(_.contains("InvalidInput")) shouldBe true
    }
  }

  "ControllerState.checkIfRoundEnded for 31 points" should {
    "reset round and notify player scores when a player has 31 points" in {
      val events = ArrayBuffer.empty[String]
      val p1 = Player(points = 31.0, playersHealth = 3)
      val p2 = Player(points = 5.0, playersHealth = 3)
      val gd = GameData(2).copy(gameRunning = true, players = List(p1, p2))
      val controller = new GameController(PlayingState, gd, new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })

      val stub = new ControllerState {}
      stub.checkIfRoundEnded(controller, p1)

      events.exists(_.contains("PlayerScore(")) shouldBe true
      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(_.contains("RunningGame")) shouldBe true
      controller.state shouldBe PlayingState
    }
  }

  "ControllerState.checkIfRoundEnded when round should continue" should {
    "return false and keep state unchanged" in {
      val p1 = Player(points = 10.0, playersHealth = 3)
      val p2 = Player(points = 5.0, playersHealth = 3)
      val gd = GameData(2).copy(gameRunning = true, players = List(p1, p2))
      val controller = new GameController(PlayingState, gd, new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val stub = new ControllerState {}

      val ended = stub.checkIfRoundEnded(controller, p1)

      ended shouldBe false
      controller.state shouldBe PlayingState
    }
  }
}
