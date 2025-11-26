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
      gameController.pass(1)
      gameController.gameState.currentPlayerIndex should be(1)
      gameController.pass(2)
      gameController.gameState.currentPlayerIndex should be(2)
      gameController.pass(3)
      gameController.gameState.currentPlayerIndex should be(3)
      gameController.pass(4)
      gameController.gameState.currentPlayerIndex should be(0)
    }

    "be able to knock" in {
      gameController.knock(1)
      gameController.gameState.currentPlayerIndex should be(1)

      gameController.knock(2)
      gameController.gameState.currentPlayerIndex should be(2)

      gameController.knock(3)
      gameController.gameState.currentPlayerIndex should be(3)

      gameController.knock(4)
      // Da der nächste Spieler (1) bereits geklopft hat, endet das Spiel hier.
      // Der Index bleibt stehen, da nextPlayer das Spiel beendet.
      gameController.gameState.gameRunning should be(false)
      gameController.gameState.currentPlayerIndex should be(3)
    }

    "be able to swap" in {
      val initialTable = gameController.gameState.table
      val p1 = 1

      // 1. Swap "1" mit "1"
      gameController.swap(p1, "1", "1")
      gameController.gameState.table should not be initialTable
      gameController.gameState.currentPlayerIndex should be(1)

      // Zurück rotieren zu Spieler 1
      gameController.pass(2)
      gameController.pass(3)
      gameController.pass(4)
      gameController.gameState.currentPlayerIndex should be(0)

      // Zurück tauschen
      gameController.swap(p1, "1", "1")
      gameController.gameState.table should be(initialTable)

      // Zurück rotieren zu Spieler 1
      gameController.pass(2)
      gameController.pass(3)
      gameController.pass(4)
      gameController.gameState.currentPlayerIndex should be(0)

      // 2. Swap "alle"
      gameController.swap(p1, "alle", "1")
      gameController.gameState.table should not be initialTable

      // Zurück rotieren zu Spieler 1
      gameController.pass(2)
      gameController.pass(3)
      gameController.pass(4)
      gameController.gameState.currentPlayerIndex should be(0)

      // Zurück tauschen "alle"
      gameController.swap(p1, "alle", "1")
      gameController.gameState.table should be(initialTable)
    }
    "be able to gameFinished" in {
      val playersTurn = 1
      val beforeIndex = gameController.gameState.currentPlayerIndex
      gameController.gameFinished(playersTurn)
      gameController.gameState.currentPlayerIndex should be(beforeIndex)
    }

    "be able to initialize a game" in {
      gameController.initializeGame(playerCount)
      gameController.gameState.table should not be null
    }
  }
}