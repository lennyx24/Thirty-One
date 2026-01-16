package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.thirtyone.model.gameImplementation.InvisibleCard

class InvCardSpec extends AnyWordSpec{
  "InvisibleCard" should {

    val small = 1
    val medium = 2
    val large = 5

    "have scalable cells" in {
      InvisibleCard(small).invCell should be("    ")
      InvisibleCard(medium).invCell should be("     ")
      InvisibleCard(large).invCell should be("        ")
    }

    "have a scalable size" in {
      InvisibleCard(small).toString should be("    \n    \n")
      InvisibleCard(medium).toString should be("     \n     \n     \n")
      InvisibleCard(large).toString should be("        \n        \n        \n        \n")
    }
  }
}
