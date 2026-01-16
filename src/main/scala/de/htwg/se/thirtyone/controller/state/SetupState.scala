package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.util._

object SetupState extends ControllerState:
  var playerAmount: Int = 0
  var names: Vector[String] = Vector.empty

  private def normalizeName(name: String, index: Int): String =
    val trimmed = name.trim
    if trimmed.isEmpty then s"Player ${index + 1}" else trimmed

  private def startGame(c: ControllerInterface): Unit =
    val filledNames = names
      .padTo(playerAmount, "")
      .zipWithIndex
      .map { case (n, i) => normalizeName(n, i) }

    c.gameDataSetup(playerAmount.toString)
    c.setGameData(c.gameData.changePlayersNames(filledNames.toList))
    c.setState(PlayingState)
    for (player <- c.gameData.players) do
      c.countPoints(c, player)
      c.notifyObservers(PlayerScore(player))
    c.notifyObservers(PrintTable)
    c.notifyObservers(RunningGame(c.gameData.currentPlayer))

  def reset(): Unit =
    playerAmount = 0
    names = Vector.empty

  override def setupGame(playerCount: Int, playerNames: List[String], c: ControllerInterface): Unit =
    if playerCount < 2 || playerCount > 4 then
      c.notifyObservers(InvalidInput)
    else
      playerAmount = playerCount
      names = playerNames.take(playerAmount).toVector
      startGame(c)

  override def execute(input: String, c: ControllerInterface): Unit =
    if playerAmount == 0 then
      val count = input.toIntOption.getOrElse(0)
      if count < 2 || count > 4 then
        c.notifyObservers(InvalidInput)
      else
        playerAmount = count
        names = Vector.empty
        c.notifyObservers(PlayerName(1))
    else if names.length < playerAmount then
      val idx = names.length
      val name = normalizeName(input, idx)
      names = names :+ name
      c.notifyObservers(PlayerNameSet(idx + 1, name))
      if names.length == playerAmount then
        startGame(c)
      else
        c.notifyObservers(PlayerName(names.length + 1))
    else
      c.notifyObservers(InvalidInput)

  override def selectNumber(idx: String, c: ControllerInterface): Unit =
    execute(idx, c)
