package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

class CardSpec extends AnyWordSpec{
  "Card" should {
    "have a scalable bar" in {
      val c1 = Card('d', "K", 6).bar should be("+------+ ")
      val c2 = Card('d', "K", 4).bar should be("+----+ ")
      val c3 = Card('d', "K", 5).bar should be("+-----+ ")
    }

    "have a scalable top cell" in {
      val c1 = Card('d', "K", 6).topCell should be("| dK   | ")
      val c2 = Card('d', "7", 4).topCell should be("| d7 | ")
      val c3 = Card('d', "4", 5).topCell should be("| d4  | ")
    }

    "have a scalable cells" in {
      val c1 = Card('d', "K", 6).cells should be("|      | ")
      val c2 = Card('d', "K", 4).cells should be("|    | ")
      val c3 = Card('d', "K", 5).cells should be("|     | ")
    }

    "have a scalable size" in {
       val c1 = Card('d', "K", 5).cardSize should be("+-----+ \n| dK  | \n|     | \n+-----+ \n")
       val c2 = Card('d', "9", 8).cardSize should be("+--------+ \n| d9     | \n|        | \n|        | \n|        | \n+--------+ \n")
       val c3 = Card('d', "9", 10).cardSize should be("+----------+ \n| d9       | \n|          | \n|          | \n|          | \n|          | \n+----------+ \n")
    }
  }
}