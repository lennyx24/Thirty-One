package de.htwg.se.thirtyone

import de.htwg.se.thirtyone.aview._
import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.controller.state._

object ThirtyOneApp:
    def main(args: Array[String]): Unit =
        val dummyState = GameData(Table(), GameScoringStrategy.simpleScoringStrategy,0, Nil, 0, Deck().smallDeck, false, Nil)
        val controller = GameController(SetupState, dummyState)
        val view = ConsoleView(controller)

        controller.add(view)
        controller.notifyObservers(GameStarted)
    