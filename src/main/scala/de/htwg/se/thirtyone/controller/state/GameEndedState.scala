package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.util._

object GameEndedState extends ControllerState:
  override def execute(input: String, c: ControllerInterface): Unit =
    input.toLowerCase() match
      case "j" =>
        c.setState(SetupState)
        SetupState.reset()
        c.notifyObservers(GameStarted)

      case "n" =>
        System.exit(0)

      case _ =>
        c.notifyObservers(InvalidInput)
