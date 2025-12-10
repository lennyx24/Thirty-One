package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller.command.UndoManager

class GameController(var state: ControllerState, var gameData: GameData) extends Observable:
  val undoManager = new UndoManager()

  def handleInput(input: String): Unit = state.handleInput(input, this)

  def pass(): Unit = state.pass(this)
  def knock(): Unit = state.knock(this)
  def swap(): Unit = state.swap(this)

  def selectCard(idx: String): Unit = state.selectNumber(idx, this)
  def selectAll(): Unit = state.selectAll(this)

  def undo(): Unit = 
    undoManager.undoStep()
    notifyObservers(PrintTable)
    notifyObservers(RunningGame(gameData.currentPlayerIndex + 1))
  
  def redo(): Unit =
    undoManager.redoStep()
    notifyObservers(PrintTable)
    notifyObservers(RunningGame(gameData.currentPlayerIndex + 1))