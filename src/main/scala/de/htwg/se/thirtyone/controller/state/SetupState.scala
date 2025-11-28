package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.GameController

object SetupState extends ControllerState:
    override def execute(input: String, c: GameController): Unit =
        val playerAmount = input.toInt
        playerAmount match
            case 2 | 3 | 4 =>
                c.gameData = GameData(playerAmount)
                c.state = PlayingState
                val currentPlayer = c.gameData.currentPlayerIndex + 1
                for (i <- 1 to c.gameData.playerCount) c.notifyObservers(PlayerScore(i))
                c.notifyObservers(PrintTable)
                c.notifyObservers(RunningGame(currentPlayer))
            case _ =>
                c.notifyObservers(InvalidInput)