package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._

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
                    c.gameData = c.gameData.swap(c.gameData, currentPlayer, give, take)
                    checkIfGameEnded(c, currentPlayer)
                    c.state = PlayingState
                    c.notifyObservers(PrintTable)
                    c.notifyObservers(PlayerSwapped(currentPlayer))
                    c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))
                else
                    c.notifyObservers(InvalidInput)
            case _ =>
                c.notifyObservers(InvalidInput)