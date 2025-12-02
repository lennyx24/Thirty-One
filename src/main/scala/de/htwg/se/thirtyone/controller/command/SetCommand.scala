package de.htwg.se.thirtyone.controller.command

import de.htwg.se.thirtyone.controller.*

class SetCommand(controller: GameController, command: String) extends Command {
  
  override def doStep(): Unit =
    controller.handleInput(command)

  override def undoStep(): Unit =
    controller.handleInput(command)

  override def redoStep(): Unit =
    controller.handleInput(command)
}
