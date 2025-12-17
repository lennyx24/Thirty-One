package de.htwg.se.thirtyone.controller.chainOfResponsibility

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.GameController
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import scala.util._

class SwapProcessorSpec extends AnyWordSpec with Matchers {
  "SwapProcessor" should {
    "process a valid swap successfully" in {
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2))
      val currentPlayer = controller.gameData.currentPlayerIndex + 1

      val posGive = controller.gameData.cardPositions(currentPlayer)(0)
      val posReceive = controller.gameData.cardPositions(0)(0)

      val beforeGive = controller.gameData.table.get(posGive)
      val beforeReceive = controller.gameData.table.get(posReceive)

      val res = SwapProcessor.process(controller, "1", "1")
      res match
        case Success(c) =>
          // After a successful swap the cards should be exchanged
          c.gameData.table.get(posGive) shouldBe beforeReceive
          c.gameData.table.get(posReceive) shouldBe beforeGive
        case Failure(e) => fail(s"Expected Success but got Failure: $e")
    }

    "fail when one of the cells is empty (PresenceHandler)" in {
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2))
      // make table empty so presence check fails
      controller.gameData = controller.gameData.copy(table = Table())

      val res = SwapProcessor.process(controller, "1", "1")
      res match
        case Failure(_: IndexOutOfBoundsException) => succeed
        case _ => fail("Expected Failure(IndexOutOfBoundsException)")
    }
  }
}
