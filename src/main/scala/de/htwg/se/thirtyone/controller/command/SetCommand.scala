package de.htwg.se.thirtyone.controller.command

import de.htwg.se.thirtyone.controller.*
import de.htwg.se.thirtyone.controller.state.*
import de.htwg.se.thirtyone.model.*

class SetCommand(controller: ControllerInterface, action: () => Unit) extends Command {
  var oGameData: Option[GameInterface] = None
  var oState: Option[ControllerState] = None

  override def doStep(): Unit =
    oGameData = Some(controller.gameData)
    oState = Some(controller.state)

    action()

  override def undoStep(): Unit =
    oGameData.foreach(g => controller.setGameData(g))
    oState.foreach(s => controller.setState(s))

  override def redoStep(): Unit =
    action()
}
