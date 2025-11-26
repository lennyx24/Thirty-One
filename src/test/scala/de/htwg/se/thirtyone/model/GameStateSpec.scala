package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class GameStateSpec extends AnyWordSpec {
  "GameState" should { //when für einfache dummyState verarbeitung
    "be able to switch to nextPlayer" in {
      val dummyState = GameState(Table(), 3, 1, Deck(), false, Nil)
      val nextPlayer = dummyState.nextPlayer()
      nextPlayer should be(2)
      val dummyState2 = GameState(Table(), 3, nextPlayer, Deck(), false, Nil)
      val nextPlayer2 = dummyState2.nextPlayer()
      nextPlayer2 should be(3)
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
    "be able to calculate index" in {
      val dummyState = GameState(Table(), 3, 1, Deck(), false, Nil)
      val indexToGive = "1"
      dummyState.calculateIndex(indexToGive) should be(0)
      val indexToGive2 = "2"
      dummyState.calculateIndex(indexToGive2) should be(1)
      val indexToGive3 = "3"
      dummyState.calculateIndex(indexToGive3) should be(2)
    }
    "be able to swap" in {
      val cardPositions = List(
        List((1, 3), (1, 4), (1, 5)), //Position Middle Cards
        List((0, 1), (0, 2), (0, 3)), //Position Player 1
        List((0, 5), (0, 6), (0, 7)), //Position Player 2
        List((2, 5), (2, 6), (2, 7)), //Position Player 3
        List((2, 1), (2, 2), (2, 3)), //Position Player 4
      )
      val playersTurn = 1
      val deck = Deck()
      val indexes = deck.deck.indices.toVector
      val table = Table().createGameTable(4, indexes, cardPositions, deck)
      val dummyState = GameState(table, 4, playersTurn, deck, true, cardPositions)

      val ds2 = dummyState.swap(dummyState, playersTurn, "1", "1")
      ds2.table should not be table //table geändert
      val ds3 = ds2.pass(ds2.currentPlayer) //3x passen, dass wieder spieler 1 ist
      val ds4 = ds3.pass(ds3.currentPlayer)
      val ds5 = ds4.pass(ds4.currentPlayer)
      val ds6 = ds5.swap(ds5, ds5.currentPlayer, "1", "1")
      ds6.table should be(table) //table wieder wie am Anfang

      val ds7 = ds6.swap(ds6, ds6.currentPlayer, "alle", "1")
      ds7.table should not be table //table geändert
      val ds8 = ds7.pass(ds7.currentPlayer) //3x passen, dass wieder spieler 1 ist
      val ds9 = ds8.pass(ds8.currentPlayer)
      val ds10 = ds9.pass(ds9.currentPlayer)
      val ds11 = ds10.swap(ds10, ds10.currentPlayer, "alle", "1")
      ds11.table should be(table) //table wieder wie am Anfang
    }
  }
} 