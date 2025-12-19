package de.htwg.se.thirtyone.controller.command

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.command.SetCommand
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.controller.state._

class SetCommandSpec extends AnyWordSpec with Matchers {
  "SetCommand" should {
    "store and restore controller state and gamedata on undo/redo" in {
      val controller = new de.htwg.se.thirtyone.controller.GameController(SetupState, GameData(2))
      val initialState = controller.state
      val initialGameData = controller.gameData

      def action(): Unit =
        controller.state = PlayingState
        controller.gameData = controller.gameData.pass()

      val cmd = new SetCommand(controller, () => action())

      cmd.doStep()
      controller.state shouldBe PlayingState

      cmd.undoStep()
      controller.state shouldBe initialState
      controller.gameData shouldBe initialGameData

      cmd.redoStep()
      controller.state shouldBe PlayingState
    }
  }
}

