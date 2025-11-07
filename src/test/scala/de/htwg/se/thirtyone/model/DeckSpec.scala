// scala
package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class DeckSpec extends AnyWordSpec{
  "Deck" should {
    "have a size" in {
      Deck(10).deck should be(Vector(
        Card('d',"2",10), Card('d',"3",10), Card('d',"4",10), Card('d',"5",10), Card('d',"6",10), Card('d',"7",10), Card('d',"8",10), Card('d',"9",10), Card('d',"10",10), Card('d',"J",10), Card('d',"Q",10), Card('d',"K",10), Card('d',"A",10),
        Card('s',"2",10), Card('s',"3",10), Card('s',"4",10), Card('s',"5",10), Card('s',"6",10), Card('s',"7",10), Card('s',"8",10), Card('s',"9",10), Card('s',"10",10), Card('s',"J",10), Card('s',"Q",10), Card('s',"K",10), Card('s',"A",10),
        Card('h',"2",10), Card('h',"3",10), Card('h',"4",10), Card('h',"5",10), Card('h',"6",10), Card('h',"7",10), Card('h',"8",10), Card('h',"9",10), Card('h',"10",10), Card('h',"J",10), Card('h',"Q",10), Card('h',"K",10), Card('h',"A",10),
        Card('c',"2",10), Card('c',"3",10), Card('c',"4",10), Card('c',"5",10), Card('c',"6",10), Card('c',"7",10), Card('c',"8",10), Card('c',"9",10), Card('c',"10",10), Card('c',"J",10), Card('c',"Q",10), Card('c',"K",10), Card('c',"A",10)
      ))
    }
  }
}
