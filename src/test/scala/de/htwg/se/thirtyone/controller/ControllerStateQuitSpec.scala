package de.htwg.se.thirtyone.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ControllerStateQuitSpec extends AnyWordSpec with Matchers {
  "ControllerState" should {
    "skip quit-branch test because System.exit cannot be captured reliably" in {
      cancel("Skipping quit-branch test: exercising System.exit(0) requires a separate JVM or product-code change (injectable exit handler). To test this branch reliably either: (1) make System.exit injectable, or (2) provide a small Java helper class in test sources and run it in a separate process.")
    }
  }
}
