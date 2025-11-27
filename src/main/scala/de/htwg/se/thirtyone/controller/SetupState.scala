package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._

object SetupState extends ControllerState:
    override def execute(input: String, c: GameController): Unit =
        val playerAmount = input.toInt
        playerAmount match
            case 2 | 3 | 4 =>
                c.gameData = GameData(playerAmount)
                c.state = PlayingState
                val currentPlayer = c.gameData.currentPlayerIndex + 1
                c.notifyObservers(PrintTable)
                c.notifyObservers(RunningGame(currentPlayer))
            case _ =>
                c.notifyObservers(InvalidInput)
        