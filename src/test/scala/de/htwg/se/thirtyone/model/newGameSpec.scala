package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class newGameSpec extends AnyWordSpec {
  "GameState.apply" should {
    "be able to start an apply" in {
      val seed = 42L
      scala.util.Random.setSeed(seed)

      val playerCount = 4
      val gameState = GameState(playerCount)

      // Dieselbe Seed-Position wiederherstellen, dann die gleichen Indizes mit dem tatsächlich verwendeten Deck erzeugen
      scala.util.Random.setSeed(seed)
      val indexes = Table().indexes(gameState.deck)

      val cardPositions = List(
        List((1, 3), (1, 4), (1, 5)), // Middle
        List((0, 1), (0, 2), (0, 3)), // Player 1
        List((0, 5), (0, 6), (0, 7)), // Player 2
        List((2, 5), (2, 6), (2, 7)), // Player 3
        List((2, 1), (2, 2), (2, 3)) // Player 4
      )

      val expectedTable = Table().createGameTable(playerCount, indexes, cardPositions, gameState.deck)

      gameState.table should be(expectedTable)
      gameState.playerCount should be(playerCount)
      gameState.currentPlayerIndex should be(0)
      gameState.gameRunning should be(true)
    }
  }
}
