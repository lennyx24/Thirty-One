package de.htwg.se.thirtyone.util

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ExitHandlerSpec extends AnyWordSpec with Matchers {
  "ExitHandler" should {
    "delegate exit calls" in {
      val original = ExitHandler.exit
      var called: Option[Int] = None
      try {
        ExitHandler.exit = code => called = Some(code)
        ExitHandler.exit(7)
        called shouldBe Some(7)
      } finally {
        ExitHandler.exit = original
      }
    }
  }
}
