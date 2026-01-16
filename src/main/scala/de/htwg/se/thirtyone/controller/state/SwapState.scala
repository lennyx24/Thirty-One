package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.controller.command.SetCommand
import de.htwg.se.thirtyone.controller.chainOfResponsibility.SwapProcessor
import de.htwg.se.thirtyone.util._

import scala.util._

class SwapState extends ControllerState:
  var give: String = ""

  override def execute(input: String, c: ControllerInterface): Unit =
    input match
      case "1" | "2" | "3" | "alle" =>
        val actedIndex = c.gameData.currentPlayerIndex
        val currentPlayer = c.gameData.currentPlayer
        if give == "" then
          give = input.toLowerCase()
          if give != "alle" then c.notifyObservers(PlayerSwapTake(currentPlayer))
          else handleInput("1", c) // When input is "all" call same Method to get into else part, "1" to make recursion in GameData work and change all
        else if input != "alle" then
          val take = input
          var swapSuccess = false
          var roundEnded = false
          val command = new SetCommand(c, () => {
            SwapProcessor.process(c, give, take) match
              case Success(_) =>
                swapSuccess = true
                val actedPlayer =
                  if actedIndex >= 0 && actedIndex < c.gameData.players.length then c.gameData.players(actedIndex)
                  else currentPlayer
                c.countPoints(c, actedPlayer)
                roundEnded = checkIfRoundEnded(c, actedPlayer)
                c.setState(PlayingState)
              case Failure(_) =>
                swapSuccess = false
          })
          c.undoManager.doStep(command)

          if swapSuccess then
            val actedPlayer =
              if actedIndex >= 0 && actedIndex < c.gameData.players.length then c.gameData.players(actedIndex)
              else currentPlayer
            if !roundEnded then
              c.notifyObservers(PrintTable)
              c.notifyObservers(PlayerScore(actedPlayer))
              c.notifyObservers(PlayerSwapped(actedPlayer))
              c.notifyObservers(RunningGame(c.gameData.currentPlayer))
            give = ""
          else
            c.notifyObservers(InvalidInput)
            give = ""
        else
          c.notifyObservers(InvalidInput)
      case _ =>
        c.notifyObservers(InvalidInput)

  override def selectNumber(idx: String, c: ControllerInterface): Unit =
    execute(idx, c)

  override def selectAll(c: ControllerInterface): Unit =
    execute("alle", c)
