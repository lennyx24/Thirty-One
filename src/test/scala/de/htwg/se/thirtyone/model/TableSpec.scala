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
      val s1 = newTab1.toString
      s1 should include("10♥")
      s1 should include("+----------")
      s1 should not include ("4♦")
      s1 should not include ("7♠")

      val newTab2 = newTab1.set((0, 2), d4)
      val s2 = newTab2.toString
      s2 should include("10♥")
      s2 should include("4♦")
      s2 should include("+----------")
      s2 should not include ("7♠")

      val newTab3 = newTab2.set((1, 3), s7)
      val s3 = newTab3.toString
      s3 should include("10♥")
      s3 should include("4♦")
      s3 should include("7♠")
      s3 should include("+----------")
    }

    "be able to get its value" in {
      val newTab = tab.set((0, 0), h10).set((1, 1), d4).set((2, 0), s7)

      newTab.get((0, 0)) should be(Card('♥', "10", 10))
      newTab.get((1, 1)) should be(Card('♦', "4", 10))

      an[NoSuchElementException] should be thrownBy (newTab.get((2, 1)))
    }

    "be able to swap cards" in {
      val newTab = tab.set((0, 0), h10).set((1, 1), d4).set((2, 0), s7)

      val newTab4 = newTab.swap((0, 0), (2, 0))
      newTab4 should not be newTab
      val newTab5 = newTab4.swap((0, 0), (2, 0))
      newTab5 should be(newTab)
    }

    "be able to setAll cards" in {

      val newTab = tab.setAll(List((0, 0), (0, 1), (1, 2)), List(h10, d4, s7))
      val s = newTab.toString
      s should include("10♥")
      s should include("4♦")
      s should include("7♠")
      s should include("+----------")

      newTab.get((0, 0)) should be(Card('♥', "10", 10))
      newTab.get((0, 1)) should be(Card('♦', "4", 10))
      newTab.get((1, 2)) should be(Card('♠', "7", 10))
    }

    "should be able to createGameTable" in {
      val playercount = 2
      val deck = Deck()
      val indexes = (0 until deck.deck.size).toVector

      val cardPositions = List(
        List((1, 3), (1, 4), (1, 5)), //Position Middle Cards
        List((0, 1), (0, 2), (0, 3)), //Position Player 1
        List((0, 5), (0, 6), (0, 7)), //Position Player 2
        List((2, 5), (2, 6), (2, 7)), //Position Player 3
        List((2, 1), (2, 2), (2, 3)), //Position Player 4
      )

      val table = Table().createGameTable(playercount, indexes, cardPositions, deck)
      table.get(cardPositions(0)(0)) should be(deck.deck(0))
      table.get(cardPositions(0)(1)) should be(deck.deck(1))
      table.get(cardPositions(0)(2)) should be(deck.deck(2))
      table.get(cardPositions(1)(0)) should be(deck.deck(3))
      table.get(cardPositions(1)(1)) should be(deck.deck(4))
      table.get(cardPositions(1)(2)) should be(deck.deck(5))
    }
  }
}