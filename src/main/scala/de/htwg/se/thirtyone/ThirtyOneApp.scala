package de.htwg.se.thirtyone

import de.htwg.se.thirtyone.aview._
import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.state._
import scala.io.StdIn.readLine

object ThirtyOneApp:
    def main(args: Array[String]): Unit =
        val dummyState = GameData(Table(), GameScoringStrategy.simpleScoringStrategy,0, Nil, 0, Deck().smallDeck, Table().indexes(Deck().smallDeck),0, false, Nil)
        val controller = GameController(SetupState, dummyState)
        val view = ConsoleView(controller)
        val gui = GUI(controller)

        controller.add(view)
        controller.add(gui)
        controller.notifyObservers(GameStarted)
        
        while (true) do
            val input = readLine()
            input match
                case "passen" | "pass" | "p" => controller.pass()
                case "klopfen" | "knock" | "k" => controller.knock()
                case "tauschen" | "swap" | "s" => controller.swap()

                case "1" | "2" | "3" => controller.selectNumber(input)
                case "alle" | "all" | "a" => controller.selectAll()

                case "undo" | "u" => controller.undo()
                case "redo" | "r" => controller.redo()
                case "exit" | "quit" | "q" => System.exit(0)

                case _ => controller.handleInput(input)
