package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller.command.UndoManager

class GameController(var state: ControllerState, var gameData: GameData) extends Observable with ControllerInterface:
  val undoManager = new UndoManager()

  override def handleInput(input: String): Unit = state.handleInput(input, this)

  override def pass(): Unit = state.pass(this)
  override def knock(): Unit = state.knock(this)
  override def swap(): Unit = state.swap(this)

  override def selectNumber(idx: String): Unit = state.selectNumber(idx, this)
  override def selectAll(): Unit = state.selectAll(this)

  override def undo(): Unit = 
    undoManager.undoStep()
    notifyObservers(PrintTable)
    notifyObservers(RunningGame(gameData.currentPlayerIndex + 1))
  
  override def redo(): Unit =
    undoManager.redoStep()
    notifyObservers(PrintTable)
    notifyObservers(RunningGame(gameData.currentPlayerIndex + 1))

  override def getPlayerScore(player: Int): Double = gameData.getPlayerPoints(player)
  override def getPlayersHand(): List[Card] = gameData.table.getAll(gameData.currentPlayerIndex + 1)
  override def getPlayersHealth(player: Int): Int = gameData.players(player).playersHealth
  override def getPlayersLength(): Int = gameData.players.length

  override def getTableGrid(): Vector[Vector[Option[Card]]] = gameData.table.grid
  override def getTableCard(): List[Card] = gameData.table.getAll(0)
  override def getTableString(): String = gameData.table.printTable(gameData.players)