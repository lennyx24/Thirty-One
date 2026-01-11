package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.util._

object SetupState extends ControllerState:
  override def selectNumber(idx: String, c: ControllerInterface): Unit =
    c.gameDataSetup(idx)
    c.setState(PlayingState)
    val currentPlayer = c.gameData.currentPlayerIndex + 1
    for (i <- 1 to c.gameData.playerCount) do
      c.countPoints(c, i)
      c.notifyObservers(PlayerScore(i))
    c.notifyObservers(PrintTable)
    c.notifyObservers(RunningGame(c.gameData.currentPlayer))