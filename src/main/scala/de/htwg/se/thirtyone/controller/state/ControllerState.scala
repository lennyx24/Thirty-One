package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.GameController

trait ControllerState {
    final def handleInput(input: String, c: GameController): Unit =
        input.trim.toLowerCase match
            case "quit" | "exit" =>
                println("Spiel wird beendet...")
                System.exit(0)
            case _ =>
                execute(input, c)

    def execute(input: String, c: GameController): Unit = c.notifyObservers(InvalidInput)

    def initiateGame(playerAmount: Int, c: GameController): Unit = c.notifyObservers(InvalidInput)

    def pass(c: GameController): Unit = c.notifyObservers(InvalidInput)
    def knock(c: GameController): Unit = c.notifyObservers(InvalidInput)
    def swap(c: GameController): Unit = c.notifyObservers(InvalidInput)

    def selectNumber(idx: String, c: GameController): Unit = c.notifyObservers(InvalidInput)
    def selectAll(c: GameController): Unit = c.notifyObservers(InvalidInput)

    def checkIfGameEnded(c: GameController, currentPlayer: Int): Unit =
        if !c.gameData.gameRunning || c.gameData.getPlayerPoints(currentPlayer) == 31 then 
            c.state = GameEndedState
            // val winner = c.gameData.getBestPlayer()
            c.notifyObservers(GameEnded(currentPlayer))
}
