package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.controller.state.*
import de.htwg.se.thirtyone.model.*
import de.htwg.se.thirtyone.util.*

class GameController(var state: ControllerState, var gameData: GameInterface) extends ControllerInterface:
  val undoManager = new UndoManager()

  override def handleInput(input: String): Unit = state.handleInput(input, this)

  override def pass(): Unit = state.pass(this)

  override def knock(): Unit = state.knock(this)

  override def swap(): Unit = state.swap(this)

  override def initialGame(idx: String, playerNames: List[String]): Unit =
    selectNumber(idx)
    gameData = gameData.changePlayersNames(playerNames)

  override def selectNumber(idx: String): Unit = state.selectNumber(idx, this)

  override def selectAll(): Unit = state.selectAll(this)

  override def countPoints(c: ControllerInterface, currentPlayer: Int): Unit = gameData = c.gameData.calculatePlayerPoints(currentPlayer)

  override def setState(controllerState: ControllerState): Unit = state = controllerState

  override def gameDataSetup(idx: String): Unit = gameData = GameData(idx.toInt)

  override def dealDamage(worstPlayer: Player): Unit = gameData = gameData.doDamage(worstPlayer)

  override def resetGame(): Unit = gameData = gameData.resetNewRound()

  override def setGameData(g: GameInterface): Unit = gameData = g

  override def gamePass(): Unit = gameData = gameData.pass()

  override def gameKnock(): Unit = gameData = gameData.knock()

  override def undo(): Unit =
    undoManager.undoStep()
    notifyObservers(PrintTable)
    notifyObservers(RunningGame(gameData.currentPlayer))

  override def redo(): Unit =
    undoManager.redoStep()
    notifyObservers(PrintTable)
    notifyObservers(RunningGame(gameData.currentPlayer))