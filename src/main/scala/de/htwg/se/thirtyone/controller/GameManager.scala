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