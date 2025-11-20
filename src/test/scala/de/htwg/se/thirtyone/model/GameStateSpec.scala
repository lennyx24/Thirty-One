package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class GameStateSpec extends AnyWordSpec {
  "GameState" should {
    "be able to switch to nextPlayer" in {
      val dummyState = GameState(Table(), 3, 1, Deck(), false, Nil)
      val nextPlayer = dummyState.nextPlayer()
      nextPlayer should be(2)
      val dummyState2 = GameState(Table(), 3, nextPlayer, Deck(), false, Nil)
      val nextPlayer2 = dummyState2.nextPlayer()
      nextPlayer2 should be (3)
      val dummyState3 = GameState(Table(), 3, nextPlayer2, Deck(), false, Nil)
      val nextPlayer3 = dummyState3.nextPlayer()
      nextPlayer3 should be(1)
    }
    "be able to pass" in {
      val dummyState = GameState(Table(), 3, 1, Deck(), false, Nil)

      val playersTurn = 1
      val gameState = dummyState.pass(playersTurn)
      gameState should be(GameState(Table(), 3, 2, Deck(), false, Nil))

      val playersTurn2 = 2
      val gameState2 = gameState.pass(playersTurn2)
      gameState2 should be(GameState(Table(), 3, 3, Deck(), false, Nil))

      val playersTurn3 = 3
      val gameState3 = gameState2.pass(playersTurn3)
      gameState3 should be(GameState(Table(), 3, 1, Deck(), false, Nil))
    }
    "be able to knock" in {
      //noch nicht fertig
      val dummyState = GameState(Table(), 3, 1, Deck(), false, Nil)

      val playersTurn = 1
      val gameState = dummyState.knock(playersTurn)
      gameState should be(GameState(Table(), 3, 2, Deck(), false, Nil))

      val playersTurn2 = 2
      val gameState2 = gameState.knock(playersTurn2)
      gameState2 should be(GameState(Table(), 3, 3, Deck(), false, Nil))

      val playersTurn3 = 3
      val gameState3 = gameState2.knock(playersTurn3)
      gameState3 should be(GameState(Table(), 3, 1, Deck(), false, Nil))
    }
    "be able to swap" in {
      val cardPositions = List(
        List((1, 3), (1, 4), (1, 5)), //Position Middle Cards
        List((0, 1), (0, 2), (0, 3)), //Position Player 1
        List((0, 5), (0, 6), (0, 7)), //Position Player 2
        List((2, 5), (2, 6), (2, 7)), //Position Player 3
        List((2, 1), (2, 2), (2, 3)), //Position Player 4
      )
      val playersTurn = 2
      val deck = Deck()
      val indexes = (0 until deck.deck.size).toVector
      val table = Table().createGameTable(4, indexes, cardPositions, deck)
      val gameState = GameState(table, 4, playersTurn, Deck(), true, cardPositions)
      gameState.table should be(table)

      val pos1 = 1
      val pos2 = 1
      val swapFinished = false
      
      val gameState2 = gameState.swap(playersTurn, pos1, pos2, swapFinished)
      gameState2.table should not be(gameState.table)
      
      val gameState3 = gameState2.swap(playersTurn, pos1, pos2, swapFinished)
      gameState3.table should be(gameState.table)
      gameState3.table should not be(gameState2.table)
    }
  }
} 