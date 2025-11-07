package de.htwg.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class CardSpec extends AnyWordSpec{
  "Card" should {
    "have a scalable bar" in {
      val c1 = Card('d', "K", 1).bar should be("+-+")
      val c2 = Card('d', "K", 2).bar should be("+--+")
      val c3 = Card('d', "K", 5).bar should be("+-----+")
    }

    "have a scalable cells" in {
      val c1 = Card('d', "K", 1).cells should be("| |")
      val c2 = Card('d', "K", 2).cells should be("|  |")
      val c3 = Card('d', "K", 5).cells should be("|     |")
    }

    "have a scalable size" in {
      val c1 = Card('d', "K", 1).cardSize should be("+-+\n+-+\n")
      val c2 = Card('d', "K", 2).cardSize should be("+--+\n|  |\n+--+\n")
      val c3 = Card('d', "K", 5).cardSize should be("+-----+\n|     |\n|     |\n+-----+\n")

      /**
       * val c1 = Card('d', "K", 5).cardSize should be("+-----+\n|dK   |\n|     |\n+-----+\n")
       * val c2 = Card('d', "9", 8).cardSize should be("+--------+\n|d9      |\n|        |\n|        |\n|        |\n+--------+")
       * val c3 = Card('d', "9", 10).cardSize should be("+----------+\n|d9        |\|          |\n|          |\|          |\|          |\n+----------+")
       */
    }
  }
}