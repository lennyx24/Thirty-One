package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.util.*
import de.htwg.se.thirtyone.controller.GameController
import de.htwg.se.thirtyone.controller.command.*

object PlayingState extends ControllerState:
    override def pass(c: GameController): Unit = 
        val currentPlayer = c.gameData.currentPlayerIndex + 1
        val command = new SetCommand(c, () => {
                    c.gameData = c.gameData.pass()
                })
                c.undoManager.doStep(command)
                checkIfRoundEnded(c, currentPlayer)
                c.notifyObservers(PrintTable)
                c.notifyObservers(PlayerPassed(currentPlayer))
                c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))

    override def knock(c: GameController): Unit = 
        val currentPlayer = c.gameData.currentPlayerIndex + 1
        val command = new SetCommand(c, () => {
                    c.gameData = c.gameData.knock()
                })
                c.undoManager.doStep(command)
                checkIfRoundEnded(c, currentPlayer)
                c.notifyObservers(PrintTable)
                c.notifyObservers(PlayerKnocked(currentPlayer))
                c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))
            
    override def swap(c: GameController): Unit = 
        val currentPlayer = c.gameData.currentPlayerIndex + 1
        val command = new SetCommand(c, () => {
                    c.state = new SwapState
                })
                c.undoManager.doStep(command)

                c.notifyObservers(PlayerSwapGive(currentPlayer))