package de.htwg.se.thirtyone

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.google.inject.Guice
import de.htwg.se.thirtyone.controller.ControllerInterface

class ThirtyOneModuleSpec extends AnyWordSpec with Matchers {
  "The ThirtyOneModule" should {
    "configure the injector correctly" in {
      val injector = Guice.createInjector(new ThirtyOneModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller should not be null
      controller.getClass.getName should include ("GameController")
    }
  }
}
