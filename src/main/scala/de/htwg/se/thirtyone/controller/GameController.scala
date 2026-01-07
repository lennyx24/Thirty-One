package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller.command.UndoManager

class GameController(var state: ControllerState, var gameData: GameInterface) extends ControllerInterface:
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
    notifyObservers(RunningGame(gameData.currentPlayer))
  
  override def redo(): Unit =
    undoManager.redoStep()
    notifyObservers(PrintTable)
    notifyObservers(RunningGame(gameData.currentPlayer))