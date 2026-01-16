package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.gameImplementation.Card
import de.htwg.se.thirtyone.model.gameImplementation.Deck
import de.htwg.se.thirtyone.model.gameImplementation.Table
import play.api.libs.json.Json

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
      val cardPositions = List(
        List((1, 3), (1, 4), (1, 5)),
        List((0, 1), (0, 2), (0, 3)),
        List((0, 5), (0, 6), (0, 7))
      )
      val all = newTab.getAll(1, cardPositions)

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
        List((1, 3), (1, 4), (1, 5)),
        List((0, 1), (0, 2), (0, 3)),
        List((0, 5), (0, 6), (0, 7)),
        List((2, 5), (2, 6), (2, 7)),
        List((2, 1), (2, 2), (2, 3)),
      )

      val (table, _) = Table().createGameTable(playercount, indexes, cardPositions, deck)
      table.get(cardPositions(0)(0)) should be(deck(0))
      table.get(cardPositions(0)(1)) should be(deck(1))
      table.get(cardPositions(0)(2)) should be(deck(2))
      table.get(cardPositions(1)(0)) should be(deck(3))
      table.get(cardPositions(1)(1)) should be(deck(4))
      table.get(cardPositions(1)(2)) should be(deck(5))
    }

    "be able to generate indexes" in {
      val deck = Deck().deck
      val indexes = tab.indexes(deck)
      indexes.length should be(deck.length)
      indexes.sorted should be(deck.indices.toVector)
    }

    "be able to print table" in {
      val newTab = tab.set((0, 0), h10)
      val s = newTab.printTable(List())
      s should include("+")
      s should include("|")
    }

    "newMiddleCards should replace middle cards when enough cards available" in {
      val deck = Deck().deck
      val indexes = (0 until deck.size).toVector
      val positions = List((1,3),(1,4),(1,5))
      val player1Pos = List((0,1),(0,2),(0,3))
      val player2Pos = List((0,5),(0,6),(0,7))
      val (tableWithCards, drawIndex) = Table().createGameTable(2, indexes, List(positions, player1Pos, player2Pos), deck)
      val startDraw = 0
      val (newTable, newDraw) = tableWithCards.newMiddleCards(indexes, positions, deck, startDraw)
      newDraw shouldBe startDraw + 3
      newTable.get(positions(0)) shouldBe deck(startDraw)
    }

    "newMiddleCards should not change table when not enough cards available" in {
      val deck = Deck().deck
      val indexes = Vector(0,1)
      val positions = List((1,3),(1,4),(1,5))
      val table = Table()
      val (t2, d2) = table.newMiddleCards(indexes, positions, deck, 0)
      d2 shouldBe 0
      t2 shouldBe table
    }

    "indexes should return sequential indices for deck" in {
      val deck = Deck().deck
      val idxs = tab.indexes(deck)
      idxs.length should be(deck.length)
      idxs.sorted should equal (deck.indices.toVector)
      idxs.distinct.length should equal(idxs.length)
    }

    "swap should throw when positions are out of range" in {
      val ex = intercept[IndexOutOfBoundsException] {
        tab.swap((9,9),(10,10))
      }
      ex shouldBe a[IndexOutOfBoundsException]
    }

    "serialize to and from XML" in {
      val newTab = tab.set((0,0), h10).set((1,1), d4)
      val xml = newTab.toXml
      val loadedTab = Table.fromXml(xml)
      
      loadedTab.grid(0)(0) shouldBe Some(h10)
      loadedTab.grid(1)(1) shouldBe Some(d4)
      loadedTab.grid(2)(2) shouldBe None
    }

    "serialize to and from JSON" in {
      val newTab = tab.set((0,0), h10).set((1,1), d4)
      val json = newTab.toJson
      val loadedTab = Table.fromJson(json)

      loadedTab.grid(0)(0) shouldBe Some(h10)
      loadedTab.grid(1)(1) shouldBe Some(d4)
      loadedTab.grid(2)(2) shouldBe None
    }

    "handle malformed/empty JSON gracefully" in {
      val emptyJson = Json.obj()
      val t = Table.fromJson(emptyJson)
      t.grid.flatten.flatten shouldBe empty
    }
  }
}