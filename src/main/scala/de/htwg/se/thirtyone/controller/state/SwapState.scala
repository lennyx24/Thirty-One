package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.controller.chainOfResponsibility.SwapProcessor
import de.htwg.se.thirtyone.util._

import scala.util._

class SwapState extends ControllerState:
  var give: String = ""

  override def execute(input: String, c: ControllerInterface): Unit =
    input match
      case "1" | "2" | "3" | "alle" =>
        val currentPlayer = c.gameData.currentPlayerIndex + 1
        if give == "" then
          give = input.toLowerCase()
          if give != "alle" then c.notifyObservers(PlayerSwapTake(currentPlayer))
          else handleInput("1", c) // When input is "all" call same Method to get into else part, "1" to make recursion in GameData work and change all
        else if input != "alle" then
          val take = input
          SwapProcessor.process(c, give, take) match
            case Success(v) =>
              c.countPoints(c, currentPlayer)
              checkIfRoundEnded(c, currentPlayer)
              c.setState(PlayingState)

              c.notifyObservers(PrintTable)
              c.notifyObservers(PlayerScore(currentPlayer))
              c.notifyObservers(PlayerSwapped(currentPlayer))
              c.notifyObservers(RunningGame(c.gameData.currentPlayer))

              give = ""
            case Failure(_) =>
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