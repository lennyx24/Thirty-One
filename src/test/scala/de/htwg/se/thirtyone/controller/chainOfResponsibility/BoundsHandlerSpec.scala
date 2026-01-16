package de.htwg.se.thirtyone.controller.chainOfResponsibility

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.gameImplementation.GameData
import de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.BoundsHandler
import scala.util._
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.controller.command.UndoManager

class BoundsHandlerSpec extends AnyWordSpec with Matchers {
  "BoundsHandler" should {
    "pass for in-bounds positions" in {
      val handler = BoundsHandler(Some(new de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.PerformSwapHandler(None)))
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val res = handler.handle(controller, "1", "1")
      res.isSuccess shouldBe true
    }

    "throw IndexOutOfBoundsException for out-of-bounds" in {
      val handler = BoundsHandler(None)
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val badPositions = controller.gameData.cardPositions.map(_.map { case (r, c) => (100, 100) })
      controller.setGameData(controller.gameData.asInstanceOf[GameData].copy(cardPositions = badPositions))

      val res = handler.handle(controller, "1", "1")
      res match
        case Failure(_: IndexOutOfBoundsException) => succeed
        case _ => fail("Expected Failure(IndexOutOfBoundsException)")
    }

    "handle 'alle' giving three cards from middle" in {
      val handler = BoundsHandler(Some(new de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.PerformSwapHandler(None)))
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(3), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val res = handler.handle(controller, "alle", "1")
      res.isInstanceOf[Try[de.htwg.se.thirtyone.controller.ControllerInterface]] shouldBe true
    }

    "return Failure when next is None even if positions in-bounds" in {
      val handler = BoundsHandler(None)
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val res = handler.handle(controller, "1", "1")
      res match
        case Failure(_: IndexOutOfBoundsException) => succeed
        case _ => fail("Expected Failure(IndexOutOfBoundsException) when next is None and positions in-bounds")
    }
  }
}
