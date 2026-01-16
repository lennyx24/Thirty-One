package de.htwg.se.thirtyone.controller.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.gameImplementation.GameData
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.controller.command.UndoManager
import scala.collection.mutable.ArrayBuffer
import java.security.Permission

class GameEndedStateSpec extends AnyWordSpec with Matchers {
  "GameEndedState" should {
    "transition to SetupState on 'j'" in {
      val events = ArrayBuffer.empty[String]
      val controller = new GameController(GameEndedState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })

      GameEndedState.execute("j", controller)
      controller.state shouldBe SetupState
      events.exists(_.contains("GameStarted")) shouldBe true
      SetupState.playerAmount shouldBe 0
    }

    "notify InvalidInput on other input" in {
      val events = ArrayBuffer.empty[String]
      val controller = new GameController(GameEndedState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })

      GameEndedState.execute("x", controller)
      events.exists(_.contains("InvalidInput")) shouldBe true
    }

    "call System.exit on 'n'" in {
      class NoExitSecurityManager extends SecurityManager {
        override def checkPermission(perm: Permission): Unit = ()
        override def checkPermission(perm: Permission, context: Object): Unit = ()
        override def checkExit(status: Int): Unit = throw new SecurityException(status.toString)
      }

      val controller = new GameController(GameEndedState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val original = System.getSecurityManager
      try {
        System.setSecurityManager(new NoExitSecurityManager)
        an [SecurityException] should be thrownBy GameEndedState.execute("n", controller)
      } finally {
        System.setSecurityManager(original)
      }
    }
  }
}
