package de.htwg.se.thirtyone

import de.htwg.se.thirtyone.aview._
import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.model._

object ThirtyOneApp:
    def main(args: Array[String]): Unit =
        val dummyState = GameState(Table(), 0, 0, Deck(), false, Nil)
        val controller = GameManager(dummyState)
        val view = ConsoleView(controller)

        controller.add(view)
        controller.notifyObservers(GameStarted)
    