package de.htwg.se.thirtyone.controller.chainOfResponsibility

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.GameController
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import scala.util._
import de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.PresenceHandler

class PresenceHandlerSpec extends AnyWordSpec with Matchers {
  "PresenceHandler" should {
    "pass when both cells present" in {
      val handler = PresenceHandler(Some(new de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.PerformSwapHandler(None)))
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2))

      val res = handler.handle(controller, "1", "1")
      res.isSuccess shouldBe true
    }

    "throw IndexOutOfBoundsException when a cell is empty" in {
      val handler = PresenceHandler(None)
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2))
      // set empty table via setGameData
      controller.setGameData(controller.gameData.asInstanceOf[GameData].copy(table = Table()))

      val res = handler.handle(controller, "1", "1")
      res match
        case Failure(_: IndexOutOfBoundsException) => succeed
        case _ => fail("Expected Failure(IndexOutOfBoundsException)")
    }
  }
}
