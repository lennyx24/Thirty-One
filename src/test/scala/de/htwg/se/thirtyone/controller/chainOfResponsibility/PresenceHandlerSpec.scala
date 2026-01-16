package de.htwg.se.thirtyone.controller.chainOfResponsibility

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.model.gameImplementation.{GameData, Table}
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.util._
import scala.util._
import de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.PresenceHandler

class PresenceHandlerSpec extends AnyWordSpec with Matchers {
  "PresenceHandler" should {
    "pass when both cells present" in {
      val handler = PresenceHandler(Some(new de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.PerformSwapHandler(None)))
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)

      val res = handler.handle(controller, "1", "1")
      res.isSuccess shouldBe true
    }

    "throw IndexOutOfBoundsException when a cell is empty" in {
      val handler = PresenceHandler(None)
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.setGameData(controller.gameData.asInstanceOf[GameData].copy(table = Table()))

      val res = handler.handle(controller, "1", "1")
      res match
        case Failure(_: IndexOutOfBoundsException) => succeed
        case _ => fail("Expected Failure(IndexOutOfBoundsException)")
    }

    "return Failure when next is None but cells present (no next handler)" in {
      val handler = PresenceHandler(None)
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val res = handler.handle(controller, "1", "1")
      res match
        case Failure(_: IndexOutOfBoundsException) => succeed
        case _ => fail("Expected Failure(IndexOutOfBoundsException) when next is None")
    }

    "handle 'alle' path when both cells present" in {
      val handler = PresenceHandler(Some(new de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.PerformSwapHandler(None)))
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)

      val res = handler.handle(controller, "alle", "1")
      res.isSuccess shouldBe true
    }
  }
}
