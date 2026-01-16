package de.htwg.se.thirtyone.aview

import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._

case class ConsoleView(controller: ControllerInterface) extends Observer:    
    override def update(event: GameEvent): Unit = event match
        case GameStarted =>
            println("-- Willkommen zu Thirty One, auch bekannt als Schwimmen! --")
            println("Wie viele Spieler seit ihr (2-4):")

        case InvalidInput =>
            print(s"Das ist keine valide Option\n: ")

        case PrintTable =>
            printNewRound(controller.gameData.table.printTable(controller.gameData.players))

        case PlayerScore(player) =>
            val points = controller.gameData.getPlayerPoints(player)
            println(s"Spieler ${player.name} hat $points Punkte.")

        case RunningGame(player) =>
            println(s"${player.name} ist dran, welchen Zug willst du machen? (Passen, Klopfen, Tauschen):")

        case PlayerSwapGive(player) =>
            println(s"${player.name}, welche Karte willst du abgeben? (1, 2, 3 oder alle):")

        case PlayerSwapTake(player) =>
            println(s"Welche Karte willst du dafür erhalten? (1, 2 oder 3):")

        case PlayerPassed(player) =>
            println(s"${player.name} setzt diese Runde aus.")

        case PlayerKnocked(player) =>
            println(s"${player.name} hat diese Runde geklopft, es darf jeder noch einen Zug machen!")

        case PlayerSwapped(player) =>
            println(s"${player.name} tauscht diese Runde.")

        case GameEnded(player) =>
            println(s"${player.name} hat die Runde gewonnen. Glückwunsch!")
            println("Wollt ihr noch eine Runde spielen? (j/n):")

        case PlayerName(player) =>
            println(s"Name für Spieler $player: ")

        case PlayerNameSet(index, name) =>
            println(s"Spieler $index heißt nun $name.")

    def printNewRound(gameTable: String): Unit = 
        (1 until 20).foreach(x => println)
        print(gameTable)
        (1 until 5).foreach(x => println)
