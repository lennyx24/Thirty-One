package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.BeforeAndAfterEach

class GameControllerSpec extends AnyWordSpec with BeforeAndAfterEach {
  var gameController: GameController = _
  val playerCount = 4

  override def beforeEach(): Unit = {
    gameController = GameController(GameState(playerCount))
  }

  "GameController" should {
    "be able to pass" in {
      // Initial ist Spieler 1 (Index 0) dran
      val p1 = 1
      gameController.pass(p1)
      gameController.gameState.currentPlayerIndex should be(1)

      val p2 = 2
      gameController.pass(p2)
      gameController.gameState.currentPlayerIndex should be(2)

      val p3 = 3
      gameController.pass(p3)
      gameController.gameState.currentPlayerIndex should be(3)

      val p4 = 4
      gameController.pass(p4)
      gameController.gameState.currentPlayerIndex should be(0)
    }

    "be able to knock" in {
      val p1 = 1
      gameController.knock(p1)
      gameController.gameState.currentPlayerIndex should be(1)

      val p2 = 2
      gameController.knock(p2)
      gameController.gameState.currentPlayerIndex should be(2)

      val p3 = 3
      gameController.knock(p3)
      gameController.gameState.currentPlayerIndex should be(3)

      val p4 = 4
      gameController.knock(p4)
      gameController.gameState.currentPlayerIndex should be(0)
    }

    "be able to swap" in {
      // Speichern des initialen Tisches zum Vergleich
      val initialTable = gameController.gameState.table
      val p1 = 1

      // 1. Swap durchführen ("1" gegen "1")
      gameController.swap(p1, "1", "1")
      gameController.gameState.table should not be initialTable
      gameController.gameState.currentPlayerIndex should be(1)

      gameController.pass(gameController.gameState.currentPlayerIndex + 1) // P2 -> P3
      gameController.pass(gameController.gameState.currentPlayerIndex + 1) // P3 -> P4
      gameController.pass(gameController.gameState.currentPlayerIndex + 1) // P4 -> P1
      gameController.gameState.currentPlayerIndex should be(0)

      // Zurück tauschen ("1" mit "1")
      gameController.swap(p1, "1", "1")
      gameController.gameState.table should be(initialTable)

      gameController.pass(gameController.gameState.currentPlayerIndex + 1) // P2 -> P3
      gameController.pass(gameController.gameState.currentPlayerIndex + 1) // P3 -> P4
      gameController.pass(gameController.gameState.currentPlayerIndex + 1) // P4 -> P1
      gameController.gameState.currentPlayerIndex should be(0)

      // "alle" tauschen
      gameController.swap(p1, "alle", "1")
      gameController.gameState.table should not be initialTable

      gameController.pass(gameController.gameState.currentPlayerIndex + 1)
      gameController.pass(gameController.gameState.currentPlayerIndex + 1)
      gameController.pass(gameController.gameState.currentPlayerIndex + 1)
      gameController.gameState.currentPlayerIndex should be(0)

      gameController.swap(p1, "alle", "1")
      gameController.gameState.table should be(initialTable)
    }

    "be able to initialize a game" in {
      gameController.initializeGame(playerCount)
      gameController.gameState.table should not be Nil
    }
  }
}