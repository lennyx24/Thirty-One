package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.ControllerInterface

object SetupState extends ControllerState:
    override def selectNumber(idx: String, c: ControllerInterface): Unit = 
        c.gameData = GameData(idx.toInt)
        c.state = PlayingState
        val currentPlayer = c.gameData.currentPlayerIndex + 1
        for (i <- 1 to c.gameData.playerCount) do
            c.gameData = c.gameData.calculatePlayerPoints(i)
            c.notifyObservers(PlayerScore(i))
        c.notifyObservers(PrintTable)
        c.notifyObservers(RunningGame(c.gameData.currentPlayer))