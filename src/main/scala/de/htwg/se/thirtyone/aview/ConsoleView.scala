package de.htwg.se.thirtyone.aview

import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.util._

case class ConsoleView(controller: ControllerInterface) extends Observer:
    private def findPlayer(info: PlayerInfo) =
        controller.gameData.players.find(_.id == info.id)

    override def update(event: GameEvent): Unit = event match
        case GameStarted =>
            println("-- Willkommen zu Thirty One, auch bekannt als Schwimmen! --")
            println("Wie viele Spieler seit ihr (2-4):")

        case InvalidInput =>
            print(s"Das ist keine valide Option\n: ")

        case PrintTable =>
            val currentPlayer = controller.gameData.currentPlayer
            printNewRound(controller.gameData.table.printTable(controller.gameData.players, currentPlayer, controller.gameData.playerPositions(currentPlayer), controller.gameData.getTableCard()))

        case PlayerScore(playerInfo) =>
            val points = findPlayer(playerInfo).map(controller.gameData.getPlayerPoints).getOrElse(0.0)
            println(s"Spieler ${playerInfo.name} hat $points Punkte.")

        case RunningGame(playerInfo) =>
            println(s"${playerInfo.name} ist dran, welchen Zug willst du machen? (Passen, Klopfen, Tauschen):")

        case PlayerSwapGive(playerInfo) =>
            println(s"${playerInfo.name}, welche Karte willst du abgeben? (1, 2, 3 oder alle):")

        case PlayerSwapTake(playerInfo) =>
            println(s"Welche Karte willst du dafür erhalten? (1, 2 oder 3):")

        case PlayerPassed(playerInfo) =>
            println(s"${playerInfo.name} setzt diese Runde aus.")

        case PlayerKnocked(playerInfo) =>
            println(s"${playerInfo.name} hat diese Runde geklopft, es darf jeder noch einen Zug machen!")

        case PlayerSwapped(playerInfo) =>
            println(s"${playerInfo.name} tauscht diese Runde.")

        case GameEnded(playerInfo) =>
            println(s"${playerInfo.name} hat die Runde gewonnen. Glückwunsch!")
            println("Wollt ihr noch eine Runde spielen? (j/n):")

        case RoundEnded(winnerInfo) =>
            println(s"Runde beendet. Gewinner: ${winnerInfo.name}")

        case PlayerName(player) =>
            println(s"Name für Spieler $player: ")

        case PlayerNameSet(index, name) =>
            println(s"Spieler $index heißt nun $name.")

    private def printNewRound(gameTable: String): Unit =
        for _ <- 1 until 20 do println()
        print(gameTable)
        for _ <- 1 until 5 do println()
