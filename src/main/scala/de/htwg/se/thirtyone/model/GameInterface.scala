package de.htwg.se.thirtyone.model

import de.htwg.se.thirtyone.model.GameScoringStrategy._
import scala.util.Try

trait GameInterface {
    def table: Table
    def playerCount: Int
    def players: List[Player]
    def currentPlayerIndex: Int
    def gameRunning: Boolean
    def cardPositions: List[List[(Int, Int)]]

    def currentPlayer: Player
    def nextPlayer(): GameInterface
    def calculatePlayerPoints(playerNumber: Int): GameInterface
    def getPlayersHand(): List[Card]
    def getPlayersHealth(player: Int): Int
    def getPlayerScore(player: Int): Double
    def getTableCard(): List[Card]
    def doDamage(player: Player): GameInterface
    def getPlayerPoints(playerNumber: Int): Double
    def isGameEnded: Boolean
    def getBestPlayerByPoints: Player
    def getWorstPlayerByPoints: Player
    def pass(): GameInterface
    def knock(): GameInterface
    def swap(playersTurn: Int, idxGiveString: String, idxReceiveString: String): Try[GameInterface]
    def resetNewRound(): GameInterface
}
