package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class TableSpec extends AnyWordSpec {
  "Table" should {
    val tab: Table = Table()
    val h10: Card = Card('♥', "10")
    val d4: Card = Card('♦', "4")
    val s7: Card = Card('♠', "7")

    "be able to set its value" in {
      val newTab1 = tab.set((0, 1), h10)
      newTab1.get((0, 1)) should be(h10)
      
      val newTab2 = newTab1.set((0, 2), d4)
      newTab2.get((0, 2)) should be(d4)

      val newTab3 = newTab2.set((1, 3), s7)
      newTab3.get((1, 3)) should be(s7)
    }

    "be able to get its value" in {
      val newTab = tab.set((0, 0), h10).set((1, 1), d4).set((2, 0), s7)

      newTab.get((0, 0)) should be(Card('♥', "10", 10))
      newTab.get((1, 1)) should be(Card('♦', "4", 10))

      an[NoSuchElementException] should be thrownBy (newTab.get((2, 1)))
    }

    "be able to get all values of a player" in {
      val newTab = tab.set((0, 1), h10).set((0, 2), d4).set((0, 3), s7)
      val all = newTab.getAll(1) // Player 1 (index 1 in positions list)

      all.size should be(3)
      all should be(List(h10, d4, s7))
    }

    "be able to swap cards" in {
      val newTab = tab.set((0, 0), h10).set((1, 1), d4).set((2, 0), s7)

      val newTab4 = newTab.swap((0, 0), (2, 0))
      newTab4.get((0, 0)) should be(s7)
      newTab4.get((2, 0)) should be(h10)
      
      val newTab5 = newTab4.swap((0, 0), (2, 0))
      newTab5 should be(newTab)
    }

    "be able to setAll cards" in {
      val newTab = tab.setAll(List((0, 0), (0, 1), (1, 2)), List(h10, d4, s7))
      
      newTab.get((0, 0)) should be(Card('♥', "10", 10))
      newTab.get((0, 1)) should be(Card('♦', "4", 10))
      newTab.get((1, 2)) should be(Card('♠', "7", 10))
    }

    "should be able to createGameTable" in {
      val playercount = 2
      val deck = Deck().deck
      val indexes = (0 until deck.size).toVector

      val cardPositions = List(
        List((1, 3), (1, 4), (1, 5)), //Position Middle Cards
        List((0, 1), (0, 2), (0, 3)), //Position Player 1
        List((0, 5), (0, 6), (0, 7)), //Position Player 2
        List((2, 5), (2, 6), (2, 7)), //Position Player 3
        List((2, 1), (2, 2), (2, 3)), //Position Player 4
      )

      val table = Table().createGameTable(playercount, indexes, cardPositions, Deck())
      table.get(cardPositions(0)(0)) should be(deck(0))
      table.get(cardPositions(0)(1)) should be(deck(1))
      table.get(cardPositions(0)(2)) should be(deck(2))
      table.get(cardPositions(1)(0)) should be(deck(3))
      table.get(cardPositions(1)(1)) should be(deck(4))
      table.get(cardPositions(1)(2)) should be(deck(5))
    }

    "be able to generate indexes" in {
      val deck = Deck()
      val indexes = tab.indexes(deck)
      indexes.length should be(deck.deck.length)
      indexes.sorted should be(deck.deck.indices.toVector)
    }

    "be able to print table" in {
      val newTab = tab.set((0, 0), h10)
      val s = newTab.printTable(List())
      s should include("+")
      s should include("|")
    }
  }
}