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
            val playerIndex = controller.gameData.players.indexOf(player)
            val points = controller.gameData.getPlayerPoints(playerIndex)
            println(s"Spieler $player hat $points Punkte.")

        case RunningGame(player) =>
            val playerName = player.name
            println(s"Spieler $playerName ist dran, welchen Zug willst du machen? (Passen, Klopfen, Tauschen):")

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
            println(s"Spieler ${player.name} hat die Runde gewonnen. Glückwunsch!")
            println("Wollt ihr noch eine Runde spielen? (j/n):")

        case PlayerName(player) =>
            println(s"Name für Spieler $player: ")

    def printNewRound(gameTable: String): Unit = 
        (1 until 20).foreach(x => println)
        print(gameTable)
        (1 until 5).foreach(x => println)