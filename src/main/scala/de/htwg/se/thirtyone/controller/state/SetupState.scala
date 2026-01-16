package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.util._

object SetupState extends ControllerState:
  var playerAmount: Int = 0
  var finishedPlayerCount = 0
  override def execute(input: String, c: ControllerInterface): Unit =
    if playerAmount == 0 then
      playerAmount = input.toInt
      c.gameDataSetup(input)
      c.notifyObservers(PlayerName(finishedPlayerCount + 1))
    else if finishedPlayerCount == playerAmount - 1 then
      c.setState(PlayingState)
      for (i <- c.gameData.players.indices) do
        val player = c.gameData.players(i)
        c.countPoints(c, player)
        val updatedPlayer = c.gameData.players(i)
        c.notifyObservers(PlayerScore(updatedPlayer))
      c.notifyObservers(PrintTable)
      c.notifyObservers(RunningGame(c.gameData.currentPlayer))
    else
      val player = c.gameData.players(finishedPlayerCount)
      c.changePlayerName(player, input)
      finishedPlayerCount += 1
      c.notifyObservers(PlayerName(finishedPlayerCount + 1))

  override def selectNumber(idx: String, c: ControllerInterface): Unit =
    execute(idx, c)
