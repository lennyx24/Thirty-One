package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class CardSpec extends AnyWordSpec {
  "Card" should {
    "have a scalable bar" in {
      val c1 = Card('♦', "K", 6).bar should be("+------+ ")
      val c2 = Card('♦', "K", 4).bar should be("+----+ ")
      val c3 = Card('♦', "K", 5).bar should be("+-----+ ")
    }

    "have a scalable top cell" in {
      val c1 = Card('♦', "K", 6).topCell should be("| K♦   | ")
      val c2 = Card('♦', "7", 4).topCell should be("| 7♦ | ")
      val c3 = Card('♦', "4", 5).topCell should be("| 4♦  | ")
    }

    "have scalable cells" in {
      val c1 = Card('♦', "K", 6).cells should be("|      | ")
      val c2 = Card('♦', "K", 4).cells should be("|    | ")
      val c3 = Card('♦', "K", 5).cells should be("|     | ")
    }

    "have a scalable size" in {
      val c1 = Card('♦', "K", 5).cardSize should be("+-----+ \n| K♦  | \n|     | \n+-----+ \n")
      val c2 = Card('♦', "9", 8).cardSize should be("+--------+ \n| 9♦     | \n|        | \n|        | \n|        | \n+--------+ \n")
      val c3 = Card('♦', "9").cardSize should be("+----------+ \n| 9♦       | \n|          | \n|          | \n|          | \n|          | \n+----------+ \n")
    }
  }
}