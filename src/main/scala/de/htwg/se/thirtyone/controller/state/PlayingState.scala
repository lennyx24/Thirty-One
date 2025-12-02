package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.GameController

object PlayingState extends ControllerState:
    override def execute(input: String, c: GameController): Unit =
        val currentPlayer = c.gameData.currentPlayerIndex + 1
        
        input.toLowerCase() match
            case "passen" => 
                c.gameData = c.gameData.pass()
                checkIfRoundEnded(c, currentPlayer)
                c.notifyObservers(PrintTable)
                c.notifyObservers(PlayerPassed(currentPlayer))
                c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))

            case "klopfen" =>
                c.gameData = c.gameData.knock()
                checkIfRoundEnded(c, currentPlayer)
                c.notifyObservers(PrintTable)
                c.notifyObservers(PlayerKnocked(currentPlayer))
                c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))

            case "tauschen" =>
                c.state = new SwapState
                c.notifyObservers(PlayerSwapGive(currentPlayer))

            case _ =>
                c.notifyObservers(InvalidInput)

    