package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class GameStateSpec extends AnyWordSpec {
  val gameState = GameState(4)

  "GameState" should {
    "be able to switch to nextPlayer" in {
      val state1 = gameState
      val nextState1 = state1.nextPlayer()
      nextState1.currentPlayerIndex should be(1)

      val nextState2 = nextState1.nextPlayer()
      nextState2.currentPlayerIndex should be(2)

      val nextState3 = nextState2.nextPlayer()
      nextState3.currentPlayerIndex should be(3)

      val nextState4 = nextState3.nextPlayer()
      nextState4.currentPlayerIndex should be(0)
    }

    "be able to pass" in {
      val state1 = gameState
      val playersTurn = 1 // Spieler 1 ist dran

      // Spieler 1 passt
      val state2 = state1.pass(playersTurn)
      state2.currentPlayerIndex should be(1)

      // Spieler 2 passt
      val state3 = state2.pass(state2.currentPlayerIndex + 1)
      state3.currentPlayerIndex should be(2)

      // Spieler 3 passt
      val state4 = state3.pass(state3.currentPlayerIndex + 1)
      state4.currentPlayerIndex should be(3)

      // Spieler 4 passt -> zurück zu Spieler 1 (Index 0)
      val state5 = state4.pass(state4.currentPlayerIndex + 1)
      state5.currentPlayerIndex should be(0)
    }

    "be able to knock" in {
      val state1 = gameState
      val playersTurn = 1

      // Spieler 1 klopft
      val state2 = state1.knock(playersTurn)
      // Nächster Spieler ist dran
      state2.currentPlayerIndex should be(1)
      
      state2.players(0).hasKnocked should be(true)

      // Spieler 2 klopft
      val state3 = state2.knock(state2.currentPlayerIndex + 1)
      state3.currentPlayerIndex should be(2)
      state3.players(1).hasKnocked should be(true)

      // Spieler 3 klopft
      val state4 = state3.knock(state3.currentPlayerIndex + 1)
      state4.currentPlayerIndex should be(3)
      state4.players(2).hasKnocked should be(true)
    }

    "be able to calculate index" in {
      val indexToGive = "1"
      gameState.calculateIndex(indexToGive) should be(0)
      val indexToGive2 = "2"
      gameState.calculateIndex(indexToGive2) should be(1)
      val indexToGive3 = "3"
      gameState.calculateIndex(indexToGive3) should be(2)
    }

    "be able to swap" in {
      // Wir speichern den initialen Table, um Änderungen zu prüfen
      val initialTable = gameState.table
      val playersTurn = 1 // Spieler 1

      // 1. Swap durchführen (Karte 1 gegen Karte 1)
      val ds2 = gameState.swap(gameState, playersTurn, "1", "1")
      ds2.table should not be initialTable // Table hat sich geändert
      ds2.currentPlayerIndex should be(1) // Spieler hat gewechselt
      
      val ds3 = ds2.pass(ds2.currentPlayerIndex + 1)
      val ds4 = ds3.pass(ds3.currentPlayerIndex + 1)
      val ds5 = ds4.pass(ds4.currentPlayerIndex + 1)
      ds5.currentPlayerIndex should be(0) // Wieder Spieler 1

      // Zurück tauschen ("1" mit "1")
      val ds6 = ds5.swap(ds5, ds5.currentPlayerIndex + 1, "1", "1")
      ds6.table should be(initialTable) // Table sollte wieder identisch sein
      
      val ds7 = ds6.pass(ds6.currentPlayerIndex + 1) // P3
      val ds8 = ds7.pass(ds7.currentPlayerIndex + 1) // P4
      val ds9 = ds8.pass(ds8.currentPlayerIndex + 1) // P1 ist wieder dran
      ds9.currentPlayerIndex should be(0)

      val ds10 = ds9.swap(ds9, ds9.currentPlayerIndex + 1, "alle", "1")
      ds10.table should not be initialTable

      // Wieder bis zu Spieler 1 rotieren
      val ds11 = ds10.pass(ds10.currentPlayerIndex + 1)
      val ds12 = ds11.pass(ds11.currentPlayerIndex + 1)
      val ds13 = ds12.pass(ds12.currentPlayerIndex + 1)
      ds13.currentPlayerIndex should be(0)

      // Zurück tauschen "alle"
      val ds14 = ds13.swap(ds13, ds13.currentPlayerIndex + 1, "alle", "1")
      ds14.table should be(initialTable) // Table sollte wieder identisch sein
    }
  }
}