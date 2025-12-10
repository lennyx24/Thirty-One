package de.htwg.se.thirtyone.aview

import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._

case class ConsoleView(controller: GameController) extends Observer:    
    override def update(event: GameEvent): Unit = event match
        case GameStarted =>
            println("-- Willkommen zu Thirty One, auch bekannt als Schwimmen! --")
            println("Wie viele Spieler seit ihr (2-4):")

        case InvalidInput =>
            print(s"Das ist keine valide Option\n: ")

        case PrintTable =>
            printNewRound(controller.gameData.table)

        case PlayerScore(player) =>
            val points = controller.gameData.getPlayerPoints(player)
            println(s"Spieler $player hat $points Punkte.")

        case RunningGame(player) =>
            println(s"Spieler $player ist dran, welchen Zug willst du machen? (Passen, Klopfen, Tauschen):")

        case PlayerSwapGive(player) =>
            println(s"Spieler $player, welche Karte willst du abgeben? (1, 2, 3 oder alle):")

        case PlayerSwapTake(player) =>
            println(s"Welche Karte willst du dafür erhalten? (1, 2 oder 3):")

        case PlayerPassed(player) =>
            println(s"Spieler $player setzt diese Runde aus.")

        case PlayerKnocked(player) =>
            println(s"Spieler $player hat diese Runde geklopft, es darf jeder noch einen Zug machen!")

        case PlayerSwapped(player) =>
            println(s"Spieler $player tauscht diese Runde.")

        case GameEnded(player) =>
            println(s"Spieler $player hat gewonnen. Glückwunsch!")
            println("Wollt ihr noch eine Runde spielen? (j/n):")

    def printNewRound(gameTable: Table): Unit = 
        (1 until 20).foreach(x => println)
        print(gameTable.printTable(controller.gameData.players))
        (1 until 5).foreach(x => println)