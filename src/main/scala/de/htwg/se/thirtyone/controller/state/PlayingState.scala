package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.util.*
import de.htwg.se.thirtyone.controller.GameController
import de.htwg.se.thirtyone.controller.command.*

object PlayingState extends ControllerState:
    override def execute(input: String, c: GameController): Unit =
        val currentPlayer = c.gameData.currentPlayerIndex + 1

        input.toLowerCase() match
            case "passen" | "pass" =>
                val command = new SetCommand(c, () => {
                    c.gameData = c.gameData.pass()
                })
                c.undoManager.doStep(command)

                checkIfGameEnded(c, currentPlayer)
                c.notifyObservers(PrintTable)
                c.notifyObservers(PlayerPassed(currentPlayer))
                c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))

            case "klopfen" | "knock" =>
                val command = new SetCommand(c, () => {
                    c.gameData = c.gameData.knock()
                })
                c.undoManager.doStep(command)

                checkIfGameEnded(c, currentPlayer)
                c.notifyObservers(PrintTable)
                c.notifyObservers(PlayerKnocked(currentPlayer))
                c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))

            case "tauschen" | "swap" =>
                val command = new SetCommand(c, () => {
                    c.state = new SwapState
                })
                c.undoManager.doStep(command)

                c.notifyObservers(PlayerSwapGive(currentPlayer))

            case "undo" =>
                c.undoManager.undoStep()
                c.notifyObservers(PrintTable)
                c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))

            case "redo" =>
                c.undoManager.redoStep()
                c.notifyObservers(PrintTable)
                c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))

            case _ =>
                c.notifyObservers(InvalidInput)