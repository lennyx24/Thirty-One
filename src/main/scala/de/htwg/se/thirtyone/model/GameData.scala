package de.htwg.se.thirtyone.model

import scala.annotation.tailrec
import de.htwg.se.thirtyone.model.factory._
import de.htwg.se.thirtyone.model.GameScoringStrategy.Strategy

case class GameData(
    table: Table,
    scoringStrategy: Strategy,
    playerCount: Int,
    players: List[Player],
    currentPlayerIndex: Int,
    deck: Deck,
    gameRunning: Boolean,
    cardPositions: List[List[(Int, Int)]]
):
    def currentPlayer(): Player = players(currentPlayerIndex)

    def nextPlayer(): GameData =
      val nextGameState =
        if currentPlayerIndex + 1 == playerCount then copy(currentPlayerIndex = 0)
        else copy(currentPlayerIndex = currentPlayerIndex + 1)

      if nextGameState.currentPlayer().hasKnocked then copy(gameRunning = false)
      else nextGameState

    def calculatePlayerPoints(player: Int): GameData =
      val cards = table.getAll(player).toList
      val p = scoringStrategy(cards)

      val playerIndex = player - 1
      val playerToUpdate = players(playerIndex)
      val newPlayer = playerToUpdate.copy(points = p)
      val newPlayers = players.updated(playerIndex, newPlayer)
      copy(players = newPlayers)
    
    def doDamage(player: Player): GameData =
      val playerIndex = players.indexOf(player)
      val newPlayer = player.receiveDamage(1)
      
      val newPlayers = players.updated(playerIndex, newPlayer)
      copy(players = newPlayers)

    def getPlayerPoints(player: Int): Double = players(player - 1).points

    def isGameEnded: Boolean = players.exists(!_.isAlive)

    def getBestPlayerByPoints: Player = players.maxBy(_.points)
    
    def getWorstPlayerByPoints: Player = players.minBy(_.points)

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

    def resetNewRound: GameData =
      val savedPlayers = players.map(_.copy(hasKnocked = false, points = 0))
      val newGame = GameData(playerCount)
      newGame.copy(players = savedPlayers)

object GameData:
    def apply(playerAmount: Int, gameMode: GameFactory = StandardGameFactory): GameData =
      gameMode.createGame(playerAmount)
        