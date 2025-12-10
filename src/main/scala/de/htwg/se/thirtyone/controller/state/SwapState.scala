package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.GameController

class SwapState extends ControllerState:
    var give: String = ""
    override def selectNumber(idx: String, c: GameController): Unit = 
        val currentPlayer = c.gameData.currentPlayerIndex + 1
        if give == "" then
            give = idx
            c.notifyObservers(PlayerSwapTake(currentPlayer))
        else
            val take = idx
            c.gameData = c.gameData.swap(c.gameData, currentPlayer, give, take)
            c.gameData = c.gameData.calculatePlayerPoints(currentPlayer)
            checkIfRoundEnded(c, currentPlayer)
            c.state = PlayingState       
            c.notifyObservers(PrintTable)
            c.notifyObservers(PlayerScore(currentPlayer))
            c.notifyObservers(PlayerSwapped(currentPlayer))
            c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))

    override def selectAll(c: GameController): Unit = 
        val currentPlayer = c.gameData.currentPlayerIndex + 1
        c.gameData = c.gameData.swap(c.gameData, currentPlayer, "alle", "1")
        c.gameData = c.gameData.calculatePlayerPoints(currentPlayer)
        checkIfRoundEnded(c, currentPlayer)
        c.state = PlayingState
        c.notifyObservers(PrintTable)
        c.notifyObservers(PlayerScore(currentPlayer))
        c.notifyObservers(PlayerSwapped(currentPlayer))
        c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))