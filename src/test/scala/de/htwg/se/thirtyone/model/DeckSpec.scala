package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class DeckSpec extends AnyWordSpec{
  "Deck" should {
    "have a size" in {
      Deck().deck should be(Vector(
        Card('♦',"2",10), Card('♦',"3",10), Card('♦',"4",10), Card('♦',"5",10), Card('♦',"6",10), Card('♦',"7",10), Card('♦',"8",10), Card('♦',"9",10), Card('♦',"10",10), Card('♦',"J",10), Card('♦',"Q",10), Card('♦',"K",10), Card('♦',"A",10),
        Card('♠',"2",10), Card('♠',"3",10), Card('♠',"4",10), Card('♠',"5",10), Card('♠',"6",10), Card('♠',"7",10), Card('♠',"8",10), Card('♠',"9",10), Card('♠',"10",10), Card('♠',"J",10), Card('♠',"Q",10), Card('♠',"K",10), Card('♠',"A",10),
        Card('♥',"2",10), Card('♥',"3",10), Card('♥',"4",10), Card('♥',"5",10), Card('♥',"6",10), Card('♥',"7",10), Card('♥',"8",10), Card('♥',"9",10), Card('♥',"10",10), Card('♥',"J",10), Card('♥',"Q",10), Card('♥',"K",10), Card('♥',"A",10),
        Card('♣',"2",10), Card('♣',"3",10), Card('♣',"4",10), Card('♣',"5",10), Card('♣',"6",10), Card('♣',"7",10), Card('♣',"8",10), Card('♣',"9",10), Card('♣',"10",10), Card('♣',"J",10), Card('♣',"Q",10), Card('♣',"K",10), Card('♣',"A",10)
      ))

      Deck(15).deck should be(Vector(
        Card('♦', "2", 15), Card('♦', "3", 15), Card('♦', "4", 15), Card('♦', "5", 15), Card('♦', "6", 15), Card('♦', "7", 15), Card('♦', "8", 15), Card('♦', "9", 15), Card('♦', "10", 15), Card('♦', "J", 15), Card('♦', "Q", 15), Card('♦', "K", 15), Card('♦', "A", 15),
        Card('♠', "2", 15), Card('♠', "3", 15), Card('♠', "4", 15), Card('♠', "5", 15), Card('♠', "6", 15), Card('♠', "7", 15), Card('♠', "8", 15), Card('♠', "9", 15), Card('♠', "10", 15), Card('♠', "J", 15), Card('♠', "Q", 15), Card('♠', "K", 15), Card('♠', "A", 15),
        Card('♥', "2", 15), Card('♥', "3", 15), Card('♥', "4", 15), Card('♥', "5", 15), Card('♥', "6", 15), Card('♥', "7", 15), Card('♥', "8", 15), Card('♥', "9", 15), Card('♥', "10", 15), Card('♥', "J", 15), Card('♥', "Q", 15), Card('♥', "K", 15), Card('♥', "A", 15),
        Card('♣', "2", 15), Card('♣', "3", 15), Card('♣', "4", 15), Card('♣', "5", 15), Card('♣', "6", 15), Card('♣', "7", 15), Card('♣', "8", 15), Card('♣', "9", 15), Card('♣', "10", 15), Card('♣', "J", 15), Card('♣', "Q", 15), Card('♣', "K", 15), Card('♣', "A", 15)
      ))
    }
  }
}