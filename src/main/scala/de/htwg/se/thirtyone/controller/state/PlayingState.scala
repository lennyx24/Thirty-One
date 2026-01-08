package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.controller.command.*
import de.htwg.se.thirtyone.util.*

object PlayingState extends ControllerState:
  override def pass(c: ControllerInterface): Unit =
    val currentPlayer = c.gameData.currentPlayerIndex + 1
    val command = new SetCommand(c, () => {
      c.gamePass()
    })
    c.undoManager.doStep(command)
    checkIfRoundEnded(c, currentPlayer)
    c.notifyObservers(PrintTable)
    c.notifyObservers(PlayerPassed(currentPlayer))
    c.notifyObservers(RunningGame(c.gameData.currentPlayer))

  override def knock(c: ControllerInterface): Unit =
    val currentPlayer = c.gameData.currentPlayerIndex + 1
    val command = new SetCommand(c, () => {
      c.gameKnock()
    })
    c.undoManager.doStep(command)
    checkIfRoundEnded(c, currentPlayer)
    c.notifyObservers(PrintTable)
    c.notifyObservers(PlayerKnocked(currentPlayer))
    c.notifyObservers(RunningGame(c.gameData.currentPlayer))

  override def swap(c: ControllerInterface): Unit =
    val currentPlayer = c.gameData.currentPlayerIndex + 1
    val command = new SetCommand(c, () => {
      c.setState(SwapState())
    })
    c.undoManager.doStep(command)

    c.notifyObservers(PlayerSwapGive(currentPlayer))