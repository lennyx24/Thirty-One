package de.htwg.se.thirtyone.controller.command

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.command._

class UndoManagerStacksSpec extends AnyWordSpec with Matchers {
  "UndoManager stacks" should {
    "handle multiple do/undo/redo transitions" in {
      var calls = List.empty[String]
      class FakeCommand(name: String) extends Command {
        def doStep(): Unit = calls = calls :+ (name + "-do")
        def undoStep(): Unit = calls = calls :+ (name + "-undo")
        def redoStep(): Unit = calls = calls :+ (name + "-redo")
      }

      val m = new UndoManager()
      val c1 = new FakeCommand("c1")
      val c2 = new FakeCommand("c2")

      m.doStep(c1)
      m.doStep(c2)
      calls should contain inOrderOnly ("c1-do", "c2-do")

      m.undoStep()
      calls.last shouldBe "c2-undo"

      m.undoStep()
      calls.last shouldBe "c1-undo"

      m.redoStep()
      calls.last shouldBe "c1-redo"

      m.redoStep()
      calls.last shouldBe "c2-redo"
    }
  }
}

