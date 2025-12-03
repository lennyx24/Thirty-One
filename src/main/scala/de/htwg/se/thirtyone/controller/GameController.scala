package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller.command.UndoManager

class GameController(var state: ControllerState, var gameData: GameData) extends Observable:
  val undoManager = new UndoManager()

  def handleInput(input: String): Unit =
    state.handleInput(input, this)