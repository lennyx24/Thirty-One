package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class InvCardSpec extends AnyWordSpec{
  "Card" should {

    "have scalable cells" in {
      val c1 = InvisibleCard(1).invCell should be("    ")
      val c2 = InvisibleCard(2).invCell should be("     ")
      val c3 = InvisibleCard(5).invCell should be("        ")
    }

    "have a scalable size" in {
      val c1 = InvisibleCard(1).invCard should be("    \n    \n")
      val c2 = InvisibleCard(2).invCard should be("     \n     \n     \n")
      val c3 = InvisibleCard(5).invCard should be("        \n        \n        \n        \n")
    }
  }
}
