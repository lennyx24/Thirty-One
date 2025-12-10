package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.model.*
import de.htwg.se.thirtyone.util.*
import de.htwg.se.thirtyone.controller.GameController
import de.htwg.se.thirtyone.controller.chainOfResponsibility.SwapProcessor

class SwapState extends ControllerState:
    var give: String = ""
    override def execute(input: String, c: GameController): Unit =
        input match
            case "1" | "2" | "3" | "alle" =>
                val currentPlayer = c.gameData.currentPlayerIndex + 1
                if give == "" then 
                    give = input.toLowerCase()
                    if give != "alle" then c.notifyObservers(PlayerSwapTake(currentPlayer))
                    else handleInput("1", c) // When input is "all" call same Method to get into else part, "1" to make recursion in GameData work and change all
                else if input != "alle" then
                    val take = input
                    //c.gameData = c.gameData.swap(c.gameData, currentPlayer, give, take)
                    SwapProcessor.process(c, give, take)
                    c.gameData = c.gameData.calculatePlayerPoints(currentPlayer)
                    checkIfGameEnded(c, currentPlayer)
                    c.state = PlayingState
                    
                    c.notifyObservers(PrintTable)
                    c.notifyObservers(PlayerScore(currentPlayer))
                    c.notifyObservers(PlayerSwapped(currentPlayer))
                    c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))
                else
                    c.notifyObservers(InvalidInput)
            case _ =>
                c.notifyObservers(InvalidInput)