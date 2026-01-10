package de.htwg.se.thirtyone

import de.htwg.se.thirtyone.aview.*
import de.htwg.se.thirtyone.controller.*
import de.htwg.se.thirtyone.controller.state.*
import de.htwg.se.thirtyone.model.*
import de.htwg.se.thirtyone.util.*

import scala.io.StdIn.readLine

object ThirtyOneApp:
  val injector = ThirtyOneModule

  def main(args: Array[String]): Unit =
    val controller = injector.controller
    val tui = ConsoleView(controller)
    val gui = GUI(controller)

    controller.add(tui)
    controller.add(gui)
    controller.notifyObservers(GameStarted)

    while (true) do
      val input = readLine()
      input match
        case "passen" | "pass" | "p" => controller.pass()
        case "klopfen" | "knock" | "k" => controller.knock()
        case "tauschen" | "swap" | "s" => controller.swap()

        case "2" | "3" | "4" => controller.selectNumber(input)
        case "alle" | "all" | "a" => controller.selectAll()

        case "undo" | "u" => controller.undo()
        case "redo" | "r" => controller.redo()
        case "exit" | "quit" | "q" => System.exit(0)

        case _ => controller.handleInput(input)
