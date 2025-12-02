package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.util.*
import de.htwg.se.thirtyone.controller.GameController
import de.htwg.se.thirtyone.controller.command.*

object PlayingState extends ControllerState:
    override def execute(input: String, c: GameController): Unit =
        val currentPlayer = c.gameData.currentPlayerIndex + 1

        input.toLowerCase() match
            case "passen" | "pass" =>
                //c.gameData = c.gameData.pass()

                UndoManager().doStep(SetCommand(c,input))

                checkIfGameEnded(c, currentPlayer)
                c.notifyObservers(PrintTable)
                c.notifyObservers(PlayerPassed(currentPlayer))
                c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))

            case "klopfen" | "knock" =>
                //c.gameData = c.gameData.knock()

                UndoManager().doStep(SetCommand(c,input))

                checkIfGameEnded(c, currentPlayer)
                c.notifyObservers(PrintTable)
                c.notifyObservers(PlayerKnocked(currentPlayer))
                c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))

            case "tauschen" | "swap" =>
                UndoManager().doStep(SetCommand(c,input))
                //c.state = new SwapState
                c.notifyObservers(PlayerSwapGive(currentPlayer))

            case "undo" =>
              UndoManager().undoStep()

            case "redo" =>
              UndoManager().redoStep()

            case _ =>
                c.notifyObservers(InvalidInput)