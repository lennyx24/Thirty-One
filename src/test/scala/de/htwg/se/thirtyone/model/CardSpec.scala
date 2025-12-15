package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class CardSpec extends AnyWordSpec {
  "Card" should {
    val cSmall = Card('♦', "K", 4)
    val cMedium = Card('♦', "K", 5)
    val cLarge = Card('♦', "K", 6)

    "have a scalable bar" in {
      cLarge.bar should be("+------+ ")
      cSmall.bar should be("+----+ ")
      cMedium.bar should be("+-----+ ")
    }

    "have a scalable top cell" in {
      Card('♦', "K", 6).topCell should be("| K♦   | ")
      Card('♦', "7", 4).topCell should be("| 7♦ | ")
      Card('♦', "4", 5).topCell should be("| 4♦  | ")
    }

    "have scalable cells" in {
      cLarge.cells should be("|      | ")
      cSmall.cells should be("|    | ")
      cMedium.cells should be("|     | ")
    }

    "have a scalable size" in {
      Card('♦', "K", 5).cardSize should be("+-----+ \n| K♦  | \n|     | \n+-----+ \n")
      Card('♦', "9", 8).cardSize should be("+--------+ \n| 9♦     | \n|        | \n|        | \n|        | \n+--------+ \n")
      Card('♦', "9").cardSize should be("+----------+ \n| 9♦       | \n|          | \n|          | \n|          | \n|          | \n+----------+ \n")
    }

    "have a cardString equal to cardSize" in {
      val c = Card('♦', "K", 5)
      c.cardString should be(c.cardSize)
    }

    "require size > 3" in {
      an [IllegalArgumentException] should be thrownBy Card('♦', "K", 3)
      noException should be thrownBy Card('♦', "K", 4)
    }
  }
}