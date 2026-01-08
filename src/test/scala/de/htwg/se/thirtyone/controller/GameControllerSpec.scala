package de.htwg.se.thirtyone.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.state._

class GameControllerSpec extends AnyWordSpec with Matchers {
  "GameController" should {
    "delegate actions to state" in {
      var called = List.empty[String]
      val mockState = new ControllerState {
        override def pass(c: ControllerInterface): Unit = { called ::= "pass" }
        override def knock(c: ControllerInterface): Unit = { called ::= "knock" }
        override def swap(c: ControllerInterface): Unit = { called ::= "swap" }
        override def selectNumber(idx: String, c: ControllerInterface): Unit = { called ::= s"select:$idx" }
        override def selectAll(c: ControllerInterface): Unit = { called ::= "selectAll" }
        override def execute(input: String, c: ControllerInterface): Unit = { called ::= s"handle:$input" }
      }
      val controller = new GameController(mockState, de.htwg.se.thirtyone.model.GameData(2))
      controller.pass()
      controller.knock()
      controller.swap()
      controller.selectNumber("3")
      controller.selectAll()
      controller.handleInput("x")
      called should contain allOf ("pass", "knock", "swap", "select:3", "selectAll", "handle:x")
    }
  }
}
