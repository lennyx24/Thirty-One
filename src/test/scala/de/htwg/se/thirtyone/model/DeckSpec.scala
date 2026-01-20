package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.thirtyone.model.game.Card
import de.htwg.se.thirtyone.model.game.Deck

class DeckSpec extends AnyWordSpec{
  "Deck" should {
    "have a size" in {
      val d10 = Deck()
      d10.deck should have length 52

      val d15 = Deck(15)
      d15.deck.head shouldBe Card('♦', "2", 15)
      d15.deck.last shouldBe Card('♣', "A", 15)
    }

    "have unique cards" in {
      val d = Deck()
      d.deck.toSet.size should be(52)
    }
  }
}