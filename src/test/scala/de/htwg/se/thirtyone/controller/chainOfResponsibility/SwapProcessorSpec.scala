package de.htwg.se.thirtyone.controller.chainOfResponsibility

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.model.gameImplementation.{GameData, Table}
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.util._
import scala.util._
import de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.{BoundsHandler, PresenceHandler, PerformSwapHandler}

class SwapProcessorSpec extends AnyWordSpec with Matchers {
  "SwapProcessor" should {
    "process a valid swap successfully" in {
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val currentPlayer = controller.gameData.currentPlayerIndex + 1

      val posGive = controller.gameData.cardPositions(currentPlayer).head
      val posReceive = controller.gameData.cardPositions(0).head

      val beforeGive = controller.gameData.table.get(posGive)
      val beforeReceive = controller.gameData.table.get(posReceive)

      val res = SwapProcessor.process(controller, "1", "1")
      res match
        case Success(c) =>
          c.gameData.table.get(posGive) should equal (beforeReceive)
          c.gameData.table.get(posReceive) should equal (beforeGive)
        case Failure(e) => fail(s"Expected Success but got Failure: $e")
    }

    "fail when one of the cells is empty (PresenceHandler)" in {
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      controller.setGameData(controller.gameData.asInstanceOf[GameData].copy(table = Table()))

      val res = SwapProcessor.process(controller, "1", "1")
      res match
        case Failure(_: IndexOutOfBoundsException) => succeed
        case _ => fail("Expected Failure(IndexOutOfBoundsException)")
    }

    "swapChain should be BoundsHandler -> PresenceHandler -> PerformSwapHandler" in {
      val chain = SwapProcessor.swapChain
      chain match
        case BoundsHandler(Some(PresenceHandler(Some(PerformSwapHandler(None))))) => succeed
        case _ => fail("swapChain does not have expected handler composition")
    }

    "fail when cardPositions entries are missing (BoundsHandler index access)" in {
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      // create cardPositions with proper outer length but empty inner lists to force IndexOutOfBounds
      val badPositions = List(List.empty[(Int, Int)], List.empty[(Int, Int)])
      controller.setGameData(controller.gameData.asInstanceOf[GameData].copy(cardPositions = badPositions))

      // BoundsHandler may throw IndexOutOfBounds directly (before returning a Failure), so accept either thrown exception
      intercept[IndexOutOfBoundsException] {
        SwapProcessor.process(controller, "1", "1")
      }
    }
  }
}
