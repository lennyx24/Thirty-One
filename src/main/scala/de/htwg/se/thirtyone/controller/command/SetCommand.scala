package de.htwg.se.thirtyone.controller.command

import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.model._

class SetCommand(controller: ControllerInterface, action: () => Unit) extends Command {
  var oGameData: Option[GameInterface] = None
  var oState: Option[ControllerState] = None
  var nGameData: Option[GameInterface] = None
  var nState: Option[ControllerState] = None

  override def doStep(): Unit =
    oGameData = Some(controller.gameData)
    oState = Some(controller.state)

    action()
    nGameData = Some(controller.gameData)
    nState = Some(controller.state)

  override def undoStep(): Unit =
    oGameData.foreach(g => controller.setGameData(g))
    oState.foreach(s => controller.setState(s))

  override def redoStep(): Unit =
    nGameData.foreach(g => controller.setGameData(g))
    nState.foreach(s => controller.setState(s))
}
