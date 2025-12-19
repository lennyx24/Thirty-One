package de.htwg.se.thirtyone.controller.chainOfResponsibility

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.BoundsHandler
import scala.util._

class BoundsHandlerSpec extends AnyWordSpec with Matchers {
  "BoundsHandler" should {
    "pass for in-bounds positions" in {
      val handler = BoundsHandler(Some(new de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.PerformSwapHandler(None)))
      val controller = new de.htwg.se.thirtyone.controller.GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2))
      val res = handler.handle(controller, "1", "1")
      res.isSuccess shouldBe true
    }

    "throw IndexOutOfBoundsException for out-of-bounds" in {
      val handler = BoundsHandler(None)
      val controller = new de.htwg.se.thirtyone.controller.GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2))
      val badPositions = controller.gameData.cardPositions.map(_.map { case (r, c) => (100, 100) })
      controller.gameData = controller.gameData.copy(cardPositions = badPositions)

      val res = handler.handle(controller, "1", "1")
      res match
        case Failure(_: IndexOutOfBoundsException) => succeed
        case _ => fail("Expected Failure(IndexOutOfBoundsException)")
    }
  }
}
