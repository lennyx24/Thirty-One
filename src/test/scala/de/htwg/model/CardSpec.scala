package de.htwg.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class CardSpec extends AnyWordSpec{
  "Card" should {
    "have a scalable bar" in {
      val c = Card(1)
       c.bar(1) should be("+-+")
       c.bar(2) should be("+--+")
       c.bar(5) should be("+-----+")
    }

    "have a scalable cells" in {
      val c = Card(1)
      c.cells(1) should be("| |")
      c.cells(2) should be("|  |")
      c.cells(5) should be("|     |")
    }

    "have a scalable size" in {
      val c = Card(1)
      c.cardSize(1) should be("+-+\n+-+\n")
      c.cardSize(2) should be("+--+\n|  |\n+--+\n")
      c.cardSize(5) should be("+-----+\n|     |\n|     |\n+-----+\n")
    }
  }
}