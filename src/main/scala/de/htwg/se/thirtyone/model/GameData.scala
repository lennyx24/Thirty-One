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
    def apply(playerCount: Int): GameData =
        val positions = List(
            List((1, 3), (1, 4), (1, 5)), //Position Middle Cards
            List((0, 1), (0, 2), (0, 3)), //Position Player 1
            List((0, 5), (0, 6), (0, 7)), //Position Player 2
            List((2, 5), (2, 6), (2, 7)), //Position Player 3
            List((2, 1), (2, 2), (2, 3)), //Position Player 4
        )
        val cardDeck = Deck()
        val indexes = Table().indexes(cardDeck)
        val gameTable = Table().createGameTable(playerCount, indexes, positions, cardDeck)

        val playersList = (1 to playerCount).map(i => Player()).toList

        GameData(
        table = gameTable,
        playerCount = playerCount,
        players = playersList,
        currentPlayerIndex = 0,
        deck = cardDeck,
        gameRunning = true,
        cardPositions = positions
    )