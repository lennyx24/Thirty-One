package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model.*

import scala.annotation.tailrec

class GameManager(var gameState: GameState) extends Observable:

  def pass(playersTurn: Int): Unit = 
    gameState = gameState.pass(playersTurn)
    notifyObservers(PlayerPassed(playersTurn))

  def knock(playersTurn: Int): Unit = 
    gameState = gameState.knock(playersTurn)
    notifyObservers(PlayerKnocked(playersTurn))

  def calculateIndex(indexToGive: String): Int = indexToGive.toInt - 1
  
  def swap(playersTurn: Int, indexGiveString: String, indexReceiveString: String): Unit =
    @tailrec
    def swapRec(currentGS: GameState, iGiveStr: String, iReceiveStr: String): GameState =
      val indexReceive = calculateIndex(iReceiveStr)
      if indexReceive > 2 then currentGS
      else
        iGiveStr match
          case "1" | "2" | "3" =>
            val indexGive = calculateIndex(iGiveStr)
            val nextGS = currentGS.swap(playersTurn, indexGive, indexReceive, true)
            swapRec(nextGS, iGiveStr, "4") // Rekursion beenden
          case "alle" =>
            val nextGS =
              if indexReceive > 1 then currentGS.swap(playersTurn, indexReceive, indexReceive, true)
              else currentGS.swap(playersTurn, indexReceive, indexReceive, false)
            val nextIndex = (indexReceive + 2).toString
            swapRec(nextGS, iGiveStr, nextIndex)

    gameState = swapRec(gameState, indexGiveString, indexReceiveString)
    notifyObservers(PlayerSwapped(playersTurn))
        
  def initializeGame(playerCount: Int): Unit = gameState = GameState.newGame(playerCount)

// object GameManager:
//   def main(args: Array[String]): Unit =
    // gameLoop(gm, 1)
    // def gameLoop(gm: GameManager, playersTurn0: Int): Unit =
    //   if !gm.gameRunning then ()
    //   else
    //     val playersTurn =
    //       if playersTurn0 > playerCount then 1 else playersTurn0

    //     gm.printNewRound(gm.gameTable)
    //     print(s"Spieler $playersTurn ist dran, welchen Zug willst du machen? (Passen, Klopfen, Tauschen): ")
    //     val playersChoice = readLine()
    //     playersChoice match
    //       case "Passen" | "passen" =>
    //         print(gm.pass(playersTurn))
    //         gameLoop(gm, playersTurn + 1) 

    //       case "Klopfen" | "klopfen" =>
    //         print(gm.knock(playersTurn))
    //         gameLoop(gm, playersTurn + 1)

    //       case "Tauschen" | "tauschen" =>
    //         print(s"Spieler $playersTurn, welche Karte möchtest du abgeben? (1, 2, 3, alle): ")
    //         val indexToGive = readLine()
    //         indexToGive match
    //           case "1" | "2" | "3" =>
    //             printf(s"Spieler $playersTurn, welche Karte möchtest du erhalten? (1, 2, 3): ")
    //             val indexToReceive = readLine()
    //             val indexReceive = gm.calculateIndex(indexToReceive)
    //             if indexReceive > 2 || indexReceive < 0 then 
    //               print(gm.InvalidMove(playersTurn))
    //               gameLoop(gm, playersTurn)
    //             val nextGm = gm.swap(playersTurn, gm.gameTable, indexToGive, indexReceive)
    //             gameLoop(nextGm, playersTurn + 1)

    //           case "alle" =>
    //             val nextGm = gm.swap(playersTurn, gm.gameTable, indexToGive, 0)
    //             gameLoop(nextGm, playersTurn + 1)

    //           case _ => 
    //             print(gm.InvalidMove(playersTurn))
    //             gameLoop(gm, playersTurn)

    //       case _ => 
            
    //         gameLoop(gm, playersTurn)
