package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util.Observable
import de.htwg.se.thirtyone.controller.state.ControllerState
import de.htwg.se.thirtyone.controller.command.UndoManager

trait ControllerInterface extends Observable {
    def gameData: GameInterface
    def state: ControllerState
    def undoManager: UndoManager

    def handleInput(input: String): Unit

    def pass(): Unit
    def knock(): Unit
    def swap(): Unit

    def selectNumber(idx: String): Unit
    def selectAll(): Unit

    def undo(): Unit
    def redo(): Unit
}
