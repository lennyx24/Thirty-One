package de.htwg.se.thirtyone.model

import scala.annotation.tailrec

case class GameState(
    table: Table,
    playerCount: Int,
    players: List[Player],
    currentPlayerIndex: Int,
    deck: Deck,
    gameRunning: Boolean,
    cardPositions: List[List[(Int, Int)]]
):
    def currentPlayer(): Player = players(currentPlayerIndex)

    def nextPlayer(): GameState = 
      if currentPlayer().hasKnocked then copy(gameRunning = false)
      else if currentPlayerIndex == playerCount then copy(currentPlayerIndex = 1)
      else copy(currentPlayerIndex = currentPlayerIndex + 1)

    def pass(playersTurn: Int): GameState = nextPlayer()
    
    def knock(playersTurn: Int): GameState = 
      val newPlayer = currentPlayer().copy(hasKnocked = true)
      val newPlayers = players.updated(playersTurn - 1, newPlayer)
      nextPlayer().copy(players= newPlayers)
    
    private def swapTable(playersTurn: Int, idx1: Int, idx2: Int, swapFinished: Boolean): GameState =
        val gs = copy(table = table.swap(cardPositions(playersTurn)(idx1), cardPositions(0)(idx2)))
        if swapFinished then gs.nextPlayer() else gs

    def calculateIndex(indexToGive: String): Int = indexToGive.toInt - 1

    def swap(currentGS: GameState, playersTurn: Int, indexGiveString: String, indexReceiveString: String): GameState =
      @tailrec
      def swapRec(currentGS: GameState, iGiveStr: String, iReceiveStr: String): GameState =
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

object GameState:
    def apply(playerCount: Int): GameState =
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

        val playersList = (1 to playerCount).map(i => Player(playersNumber = i )).toList

        GameState(
        table = gameTable,
        playerCount = playerCount,
        players = playersList,
        currentPlayerIndex = 1,
        deck = cardDeck,
        gameRunning = true,
        cardPositions = positions
    )