package de.htwg.se.thirtyone.controller.command

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UndoManagerSpec extends AnyWordSpec with Matchers {
  "UndoManager" should {
    "do, undo and redo a simple command" in {
      val um = new UndoManager()
      var didDo = false
      var didUndo = false
      var didRedo = false
      val cmd = new Command {
        override def doStep(): Unit = { didDo = true }
        override def undoStep(): Unit = { didUndo = true }
        override def redoStep(): Unit = { didRedo = true }
      }
      um.doStep(cmd)
      didDo shouldBe true
      um.undoStep()
      um.redoStep()
      succeed
    }
  }
}
