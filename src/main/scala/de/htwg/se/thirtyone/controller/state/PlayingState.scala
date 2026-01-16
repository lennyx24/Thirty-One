package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.controller.command._
import de.htwg.se.thirtyone.util._

object PlayingState extends ControllerState:
  override def pass(c: ControllerInterface): Unit =
    val actedIndex = c.gameData.currentPlayerIndex
    var roundEnded = false
    val command = new SetCommand(c, () => {
      c.gamePass()
      val actedPlayer =
        if actedIndex >= 0 && actedIndex < c.gameData.players.length then c.gameData.players(actedIndex)
        else c.gameData.currentPlayer
      roundEnded = checkIfRoundEnded(c, actedPlayer)
    })
    c.undoManager.doStep(command)
    val actedPlayer =
      if actedIndex >= 0 && actedIndex < c.gameData.players.length then c.gameData.players(actedIndex)
      else c.gameData.currentPlayer
    if !roundEnded then
      c.notifyObservers(PrintTable)
      c.notifyObservers(PlayerPassed(actedPlayer))
      c.notifyObservers(RunningGame(c.gameData.currentPlayer))

  override def knock(c: ControllerInterface): Unit =
    val actedIndex = c.gameData.currentPlayerIndex
    var roundEnded = false
    val command = new SetCommand(c, () => {
      c.gameKnock()
      val actedPlayer =
        if actedIndex >= 0 && actedIndex < c.gameData.players.length then c.gameData.players(actedIndex)
        else c.gameData.currentPlayer
      roundEnded = checkIfRoundEnded(c, actedPlayer)
    })
    c.undoManager.doStep(command)
    val actedPlayer =
      if actedIndex >= 0 && actedIndex < c.gameData.players.length then c.gameData.players(actedIndex)
      else c.gameData.currentPlayer
    if !roundEnded then
      c.notifyObservers(PrintTable)
      c.notifyObservers(PlayerKnocked(actedPlayer))
      c.notifyObservers(RunningGame(c.gameData.currentPlayer))

  override def swap(c: ControllerInterface): Unit =
    val currentPlayer = c.gameData.currentPlayer
    val command = new SetCommand(c, () => {
      c.setState(SwapState())
    })
    c.undoManager.doStep(command)

    c.notifyObservers(PlayerSwapGive(currentPlayer))