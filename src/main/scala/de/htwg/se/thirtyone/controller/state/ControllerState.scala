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

    def execute(input: String, c: GameController): Unit
        

    def checkIfGameEnded(c: GameController, currentPlayer: Int): Unit =
        if !c.gameData.gameRunning then 
            c.state = GameEndedState
            c.notifyObservers(GameEnded(currentPlayer))
}
