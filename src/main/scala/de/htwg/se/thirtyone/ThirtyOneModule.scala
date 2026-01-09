package de.htwg.se.thirtyone

import de.htwg.se.thirtyone.model.GameInterface
import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.model.factory.StandardGameFactory
import de.htwg.se.thirtyone.controller.state.SetupState
import de.htwg.se.thirtyone.controller.command.UndoManager

object ThirtyOneModule {
    val game: GameInterface = StandardGameFactory.createGame(0)

    val undoManager: UndoManager = new UndoManager()

    val controller: ControllerInterface = new GameController(SetupState, game, undoManager)
}
