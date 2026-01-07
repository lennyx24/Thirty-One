package de.htwg.se.thirtyone.controller.state

import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.ControllerInterface

object GameEndedState extends ControllerState:
    override def execute(input: String, c: ControllerInterface): Unit =
        input.toLowerCase() match
            case "j" =>
                c.state = SetupState
                c.notifyObservers(GameStarted)
            
            case "n" =>
                println("Spiel wird beendet...")
                System.exit(0)
            
            case _ =>
                c.notifyObservers(InvalidInput)