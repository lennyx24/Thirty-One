package de.htwg.se.thirtyone

import de.htwg.se.thirtyone.aview._
import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._

import scala.io.StdIn.readLine
import com.google.inject.Guice

object ThirtyOneApp:
  val injector = Guice.createInjector(new ThirtyOneModule)

  def main(args: Array[String]): Unit =
    val controller = injector.getInstance(classOf[ControllerInterface])
    val tui = ConsoleView(controller)
    val gui = GUI(controller)

    controller.add(tui)
    controller.add(gui)
    controller.notifyObservers(GameStarted)

    while (true) do
      val input = readLine()
      input match
        case "passen" | "pass" => controller.pass()
        case "klopfen" | "knock" => controller.knock()
        case "tauschen" | "swap" => controller.swap()

        case "2" | "3" | "4" => controller.selectNumber(input)
        case "alle" | "all" | "a" => controller.selectAll()

        case "undo" | "u" => controller.undo()
        case "redo" | "r" => controller.redo()
        case "exit" | "quit" | "q" => System.exit(0)

        case "save" => controller.saveGame()
        case "load" => controller.loadGame()

        case _ => controller.handleInput(input)
