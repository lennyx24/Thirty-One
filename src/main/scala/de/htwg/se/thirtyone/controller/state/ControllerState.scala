package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.model.game.Player
import de.htwg.se.thirtyone.util._

trait ControllerState:
  final def handleInput(input: String, c: ControllerInterface): Unit =
    input.trim.toLowerCase match
      case "quit" | "exit" =>
        println("Spiel wird beendet...")
        ExitHandler.exit(0)
      case _ =>
        execute(input, c)

  def execute(input: String, c: ControllerInterface): Unit = c.notifyObservers(InvalidInput)

  def pass(c: ControllerInterface): Unit = c.notifyObservers(InvalidInput)

  def knock(c: ControllerInterface): Unit = c.notifyObservers(InvalidInput)

  def swap(c: ControllerInterface): Unit = c.notifyObservers(InvalidInput)

  def selectNumber(idx: String, c: ControllerInterface): Unit = c.notifyObservers(InvalidInput)

  def selectAll(c: ControllerInterface): Unit = c.notifyObservers(InvalidInput)

  def setupGame(playerCount: Int, names: List[String], c: ControllerInterface): Unit =
    c.notifyObservers(InvalidInput)

  protected def toPlayerInfo(player: Player): PlayerInfo =
    PlayerInfo(player.id, player.name)

  def checkIfRoundEnded(c: ControllerInterface, currentPlayer: Player): Boolean =
    if !c.gameData.gameRunning || c.gameData.getPlayerPoints(currentPlayer) == 31 then
      val worstPlayer = c.gameData.getWorstPlayerByPoints
      c.dealDamage(worstPlayer)
      if c.gameData.isGameEnded then
        val bestPlayer = c.gameData.getBestPlayerByPoints
        val playerNumber = c.gameData.players.indexOf(bestPlayer) + 1
        c.setState(GameEndedState)
        c.notifyObservers(GameEnded(toPlayerInfo(bestPlayer)))
      else
        c.resetGame()
        c.setState(PlayingState)
        for (i <- c.gameData.players.indices) do
          val player = c.gameData.players(i)
          c.countPoints(c, player)
          val updatedPlayer = c.gameData.players(i)
          c.notifyObservers(PlayerScore(toPlayerInfo(updatedPlayer)))

        c.notifyObservers(PrintTable)
        c.notifyObservers(RunningGame(toPlayerInfo(c.gameData.currentPlayer)))
      true
    else
      false
