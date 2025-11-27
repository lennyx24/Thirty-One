package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.util.GameStarted
import de.htwg.se.thirtyone.util.InvalidInput

object GameEndedState extends ControllerState:
    override def execute(input: String, c: GameController): Unit =
        input.toLowerCase() match
            case "j" =>
                c.state = SetupState
                c.notifyObservers(GameStarted)
            
            case "n" =>
                println("Spiel wird beendet...")
                System.exit(0)
            
            case _ =>
                c.notifyObservers(InvalidInput)