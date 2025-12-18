package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.GameController
import de.htwg.se.thirtyone.controller.chainOfResponsibility.SwapProcessor
import scala.util._

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
          SwapProcessor.process(c, give, take) match
            case Success(v) =>
              c.gameData = v.gameData.calculatePlayerPoints(currentPlayer)
              checkIfRoundEnded(c, currentPlayer)
              c.state = PlayingState

              c.notifyObservers(PrintTable)
              c.notifyObservers(PlayerScore(currentPlayer))
              c.notifyObservers(PlayerSwapped(currentPlayer))
              c.notifyObservers(RunningGame(c.gameData.currentPlayerIndex + 1))

              give = ""
            case Failure(_) =>
              c.notifyObservers(InvalidInput)
              give = ""
        else
          c.notifyObservers(InvalidInput)
      case _ =>
        c.notifyObservers(InvalidInput)

  override def selectNumber(idx: String, c: GameController): Unit =
    // GUI calls this method directly when a card button is clicked; forward to execute so SwapState handles it consistently
    execute(idx, c)

  override def selectAll(c: GameController): Unit =
    // Forward to execute with "alle" so behavior equals console "alle"
    execute("alle", c)