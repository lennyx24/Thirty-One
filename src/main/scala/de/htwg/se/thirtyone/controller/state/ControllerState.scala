package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller._

trait ControllerState:
    final def handleInput(input: String, c: ControllerInterface): Unit =
        input.trim.toLowerCase match
            case "quit" | "exit" =>
                println("Spiel wird beendet...")
                System.exit(0)
            case _ =>
                execute(input, c)

    def execute(input: String, c: ControllerInterface): Unit = c.notifyObservers(InvalidInput)

    def pass(c: ControllerInterface): Unit = c.notifyObservers(InvalidInput)
    def knock(c: ControllerInterface): Unit = c.notifyObservers(InvalidInput)
    def swap(c: ControllerInterface): Unit = c.notifyObservers(InvalidInput)

    def selectNumber(idx: String, c: ControllerInterface): Unit = c.notifyObservers(InvalidInput)
    def selectAll(c: ControllerInterface): Unit = c.notifyObservers(InvalidInput)

    def checkIfRoundEnded(c: ControllerInterface, currentPlayer: Int): Unit =
        if !c.gameData.gameRunning || c.gameData.getPlayerPoints(currentPlayer) == 31 then
            val worstPlayer = c.gameData.getWorstPlayerByPoints
            c.gameData = c.gameData.doDamage(worstPlayer)
            if c.gameData.isGameEnded then
                val bestPlayer = c.gameData.getBestPlayerByPoints
                val playerNumber = c.gameData.players.indexOf(bestPlayer) + 1
                c.state = GameEndedState
                c.notifyObservers(GameEnded(playerNumber))
            else
                c.gameData = c.gameData.resetNewRound()
                c.state = PlayingState
                for (i <- 1 to c.gameData.playerCount) do
                  c.gameData = c.gameData.calculatePlayerPoints(i)
                  c.notifyObservers(PlayerScore(i))

                c.notifyObservers(PrintTable)
                c.notifyObservers(RunningGame(c.gameData.currentPlayer))