package de.htwg.se.thirtyone.model

import scala.annotation.tailrec

case class GameData(
    table: Table,
    playerCount: Int,
    players: List[Player],
    currentPlayerIndex: Int,
    deck: Deck,
    gameRunning: Boolean,
    cardPositions: List[List[(Int, Int)]]
):
    def currentPlayer(): Player = players(currentPlayerIndex)

    def nextPlayer(): GameData = {
      val nextGameState =
        if currentPlayerIndex + 1 == playerCount then copy(currentPlayerIndex = 0)
        else copy(currentPlayerIndex = currentPlayerIndex + 1)

      if nextGameState.currentPlayer().hasKnocked then copy(gameRunning = false)
      else nextGameState
    }

    def pass(): GameData = nextPlayer()
    
    def knock(): GameData = 
      val newPlayer = currentPlayer().copy(hasKnocked = true)
      val newPlayers = players.updated(currentPlayerIndex, newPlayer)
      copy(players = newPlayers).nextPlayer()
    
    private def swapTable(playersTurn: Int, idx1: Int, idx2: Int, swapFinished: Boolean): GameData =
        val gs = copy(table = table.swap(cardPositions(playersTurn)(idx1), cardPositions(0)(idx2)))
        if swapFinished then gs.nextPlayer() else gs

    def calculateIndex(indexToGive: String): Int = indexToGive.toInt - 1

    def swap(currentGS: GameData, playersTurn: Int, indexGiveString: String, indexReceiveString: String): GameData =
      @tailrec
      def swapRec(currentGS: GameData, iGiveStr: String, iReceiveStr: String): GameData =
        val indexReceive = calculateIndex(iReceiveStr)
        if indexReceive > 2 then currentGS
        else
          iGiveStr match
            case "1" | "2" | "3" =>
              val indexGive = calculateIndex(iGiveStr)
              val nextGS = currentGS.swapTable(playersTurn, indexGive, indexReceive, true)
              swapRec(nextGS, iGiveStr, "4") // Rekursion beenden
            case "alle" =>
              val nextGS =
                if indexReceive > 1 then currentGS.swapTable(playersTurn, indexReceive, indexReceive, true)
                else currentGS.swapTable(playersTurn, indexReceive, indexReceive, false)
              val nextIndex = (indexReceive + 2).toString
              swapRec(nextGS, iGiveStr, nextIndex)

      swapRec(currentGS, indexGiveString, indexReceiveString)

object GameData:
    def apply(playerAmount: Int, gameMode: GameFactory = StandardGameFactory): GameData =
      gameMode.createGame(playerAmount)
        