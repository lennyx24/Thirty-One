package de.htwg.se.thirtyone.aview

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.thirtyone.controller.GameController
import de.htwg.se.thirtyone.model.GameState

class ConsoleViewSpec extends AnyWordSpec with Matchers {

  "A ConsoleView" should {
    val gameState = GameState(2)
    val controller = new GameController(gameState)
    val consoleView = ConsoleView(controller)

    "return correct pass message" in {
      consoleView.pass(1) shouldBe "Spieler 1 passt diese Runde\n"
      consoleView.pass(2) shouldBe "Spieler 2 passt diese Runde\n"
    }

    "return correct knock message" in {
      consoleView.knock(1) shouldBe "Spieler 1 klopft diese Runde\n"
      consoleView.knock(3) shouldBe "Spieler 3 klopft diese Runde\n"
    }

    "return correct invalid move message" in {
      consoleView.InvalidMove(1) shouldBe "Spieler 1 das ist keine valide Option\n"
      consoleView.InvalidMove(4) shouldBe "Spieler 4 das ist keine valide Option\n"
    }
  }
}