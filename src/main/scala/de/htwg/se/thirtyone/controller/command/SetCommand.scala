package de.htwg.se.thirtyone.controller.command

import de.htwg.se.thirtyone.controller.*
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.model._

class SetCommand(controller: ControllerInterface, action: () => Unit) extends Command {
  var oGameData: Option[GameInterface] = None
  var oState: Option[ControllerState] = None
  
  override def doStep(): Unit =
    oGameData = Some(controller.gameData)
    oState = Some(controller.state)

    action()

  override def undoStep(): Unit =
    oGameData.foreach(g => controller.gameData = g)
    oState.foreach(s => controller.state = s)

  override def redoStep(): Unit =
    action()
}
