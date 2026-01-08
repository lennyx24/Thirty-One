package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.util._

class GameDataSwapInvalidGiveSpec extends AnyWordSpec with Matchers {
  "GameData.swap" should {
    "return Failure for invalid give string" in {
      val gd = GameData(2)
      val res = gd.swap(1, "invalid", "1")
      res.isFailure shouldBe true
      res match
        case Failure(e) => e.getMessage should include("Invalid give string")
        case _ => fail("Expected Failure with message 'Invalid give string'")
    }
  }
}

