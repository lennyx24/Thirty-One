package de.htwg.se.thirtyone.controller.command

import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.controller.state.ControllerState
import de.htwg.se.thirtyone.model.GameInterface

class SetCommand(controller: ControllerInterface, action: () => Unit) extends Command:
  private var beforeGameData: Option[GameInterface] = None
  private var beforeState: Option[ControllerState] = None
  private var afterGameData: Option[GameInterface] = None
  private var afterState: Option[ControllerState] = None

  override def doStep(): Unit =
    beforeGameData = Some(controller.gameData)
    beforeState = Some(controller.state)

    action()
    afterGameData = Some(controller.gameData)
    afterState = Some(controller.state)

  override def undoStep(): Unit =
    beforeGameData.foreach(controller.setGameData)
    beforeState.foreach(controller.setState)

  override def redoStep(): Unit =
    afterGameData.foreach(controller.setGameData)
    afterState.foreach(controller.setState)
