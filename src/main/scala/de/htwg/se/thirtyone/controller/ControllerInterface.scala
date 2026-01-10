package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util.Observable
import de.htwg.se.thirtyone.controller.state.ControllerState
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.model.gameImplementation.Player

trait ControllerInterface extends Observable {
    def gameData: GameInterface
    def state: ControllerState
    def undoManager: UndoManager

    def handleInput(input: String): Unit

    def pass(): Unit
    def knock(): Unit
    def swap(): Unit

    def initialGame(idx: String, playerNames: List[String]): Unit
    def selectNumber(idx: String): Unit
    def selectAll(): Unit

    def undo(): Unit
    def redo(): Unit
    
    def countPoints(c: ControllerInterface, currentPlayer: Int): Unit
    def setState(controllerState: ControllerState): Unit
    def gameDataSetup(idx: String): Unit
    def dealDamage(worstPlayer: Player): Unit
    def resetGame(): Unit
    def setGameData(g: GameInterface): Unit
    def gameKnock(): Unit
    def gamePass(): Unit
}
