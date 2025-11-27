package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.state._

class GameController(var state: ControllerState, var gameData: GameData) extends Observable:
  def handleInput(input: String): Unit =
    state.handleInput(input, this)