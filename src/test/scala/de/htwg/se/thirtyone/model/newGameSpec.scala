package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.gameImplementation.GameData
import de.htwg.se.thirtyone.model.gameImplementation.Table

class newGameSpec extends AnyWordSpec {
  "GameState.apply" should {
    "be able to start an apply" in {
      val seed = 42L
      scala.util.Random.setSeed(seed)

      val playerCount = 4
      val gameState = GameData(playerCount)

      val indexes = Table().indexes(gameState.deck)

      val cardPositions = List(
        List((1, 3), (1, 4), (1, 5)), // Middle
        List((0, 1), (0, 2), (0, 3)), // Player 1
        List((0, 5), (0, 6), (0, 7)), // Player 2
        List((2, 5), (2, 6), (2, 7)), // Player 3
        List((2, 1), (2, 2), (2, 3)) // Player 4
      )

      val table = gameState.table

      // Check high-level invariants
      gameState.playerCount should be(playerCount)
      gameState.currentPlayerIndex should be(0)
      gameState.gameRunning should be(true)

      // All expected card positions must contain a card
      val expectedPositions = cardPositions.flatten
      expectedPositions.foreach { case (r, c) =>
        table.grid(r)(c).isDefined shouldBe true
      }

      // Total number of placed cards equals sum of configured positions
      val placedCount = table.grid.flatten.count(_.isDefined)
      val expectedCount = expectedPositions.length
      placedCount shouldBe expectedCount

      // All cards placed must exist in the deck
      // val deckCards = gameState.deck.deck.toSet
      // table.grid.flatten.flatten.foreach(card => deckCards should contain(card))
    }
  }
}
