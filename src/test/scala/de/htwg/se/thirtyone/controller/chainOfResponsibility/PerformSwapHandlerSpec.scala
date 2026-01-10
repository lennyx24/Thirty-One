package de.htwg.se.thirtyone.controller.chainOfResponsibility

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.model.gameImplementation.{GameData, Table}
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.PerformSwapHandler
import scala.util._

class PerformSwapHandlerSpec extends AnyWordSpec with Matchers {
  "PerformSwapHandler" should {
    "perform swap and update gameData on success" in {
      val handler = PerformSwapHandler(None)
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2), new UndoManager())

      val posGive = controller.gameData.cardPositions(controller.gameData.currentPlayerIndex + 1).head
      val posReceive = controller.gameData.cardPositions(0).head

      val beforeGive = controller.gameData.table.get(posGive)
      val beforeReceive = controller.gameData.table.get(posReceive)

      val res = handler.handle(controller, "1", "1")
      res match
        case Success(c) =>
          c.gameData.table.get(posGive) should equal (beforeReceive)
          c.gameData.table.get(posReceive) should equal (beforeGive)
        case Failure(e) => fail(s"Expected Success but got Failure: $e")
    }

    "throw NoSuchElementException when swap fails on empty table" in {
      val handler = PerformSwapHandler(None)
      val controller = new GameController(new de.htwg.se.thirtyone.controller.state.SwapState, GameData(2), new UndoManager())
      // controller.gameData is GameInterface; set empty table via setGameData
      controller.setGameData(controller.gameData.asInstanceOf[GameData].copy(table = Table()))

      intercept[NoSuchElementException] {
        handler.handle(controller, "1", "1")
      }
    }
  }
}
