package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.controller.command.SetCommand
import de.htwg.se.thirtyone.util._

object PlayingState extends ControllerState:
  private def actedPlayer(c: ControllerInterface, index: Int) =
    if index >= 0 && index < c.gameData.players.length then c.gameData.players(index)
    else c.gameData.currentPlayer

  override def pass(c: ControllerInterface): Unit =
    val actedIndex = c.gameData.currentPlayerIndex
    var roundEnded = false
    val command = new SetCommand(c, () => {
      c.gamePass()
      val player = actedPlayer(c, actedIndex)
      roundEnded = checkIfRoundEnded(c, player)
    })
    c.undoManager.doStep(command)
    val player = actedPlayer(c, actedIndex)
    if !roundEnded then
      c.notifyObservers(PrintTable)
      c.notifyObservers(PlayerPassed(toPlayerInfo(player)))
      c.notifyObservers(RunningGame(toPlayerInfo(c.gameData.currentPlayer)))

  override def knock(c: ControllerInterface): Unit =
    val actedIndex = c.gameData.currentPlayerIndex
    var roundEnded = false
    val command = new SetCommand(c, () => {
      c.gameKnock()
      val player = actedPlayer(c, actedIndex)
      roundEnded = checkIfRoundEnded(c, player)
    })
    c.undoManager.doStep(command)
    val player = actedPlayer(c, actedIndex)
    if !roundEnded then
      c.notifyObservers(PrintTable)
      c.notifyObservers(PlayerKnocked(toPlayerInfo(player)))
      c.notifyObservers(RunningGame(toPlayerInfo(c.gameData.currentPlayer)))

  override def swap(c: ControllerInterface): Unit =
    val currentPlayer = c.gameData.currentPlayer
    val command = new SetCommand(c, () => {
      c.setState(SwapState())
    })
    c.undoManager.doStep(command)

    c.notifyObservers(PlayerSwapGive(toPlayerInfo(currentPlayer)))
