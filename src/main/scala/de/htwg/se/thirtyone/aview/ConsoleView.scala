package de.htwg.se.thirtyone.aview

import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.model._
import scala.io.StdIn.readLine

case class ConsoleView(controller: GameManager) extends Observer:    
    override def update(event: GameEvent): Unit = event match
        case GameStarted =>
            println("-- Willkommen zu Thirty One, auch bekannt als Schwimmen! --")
            println("Wie viele Spieler seit ihr (2-4):")
            val playerCount = readLine().toInt
            controller.initializeGame(playerCount)
            gameLoop()

        case PlayerPassed(player) =>
            println(s"Spieler $player setzt diese Runde aus.")

        case PlayerKnocked(player) =>
            println(s"Spieler $player hat diese Runde geklopft, es darf jeder noch einen Zug machen!")

        case PlayerSwapped(player) =>
            println(s"Spieler $player tauscht diese Runde.")
            printNewRound(controller.gameState.table)
            

    def gameLoop(): Unit =
        if !controller.gameState.gameRunning then
            println("Spiel beendet, danke fürs spielen!")
            return
        
        val currentPlayer = controller.gameState.currentPlayer
        println(s"Spieler $currentPlayer ist dran, welchen Zug willst du machen? (Passen, Klopfen, Tauschen):")
        val choice = readLine()

        choice.toLowerCase() match
            case "passen" =>
                controller.pass(currentPlayer)
                gameLoop()
            
            case "klopfen" =>
                controller.knock(currentPlayer)
                gameLoop()
            
            case "tauschen" =>
                println(s"Spieler $currentPlayer, welche Karte willst du abgeben? (1, 2, 3 oder alle):")
                val indexGive = readLine()
                indexGive match
                    case "1" | "2" | "3" =>
                        println(s"Welche Karte willst du dafür erhalten? (1, 2 oder 3):")
                        val indexReceive = readLine()
                        controller.swap(currentPlayer, indexGive, indexReceive)
                        gameLoop()
                    case "alle" =>
                        controller.swap(currentPlayer, indexGive, "0")
                        gameLoop()
                    case _ =>
                        println(InvalidMove(currentPlayer))
                        gameLoop()
            
            case _ =>
                println(InvalidMove(currentPlayer))
                gameLoop()
                 
        
    def printNewRound(gameTable: Table): Unit = 
        (1 until 20).foreach(x => println)
        print(gameTable)
        (1 until 20).foreach(x => println)

    def pass(playersTurn: Int): String = s"Spieler $playersTurn passt diese Runde\n"

    def knock(playersTurn: Int): String = s"Spieler $playersTurn klopft diese Runde\n"

    def InvalidMove(playersTurn: Int): String = s"Spieler $playersTurn das ist keine valide Option\n"