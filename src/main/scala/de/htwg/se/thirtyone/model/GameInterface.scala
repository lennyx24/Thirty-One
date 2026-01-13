package de.htwg.se.thirtyone.model

import scala.util.Try
import de.htwg.se.thirtyone.model.gameImplementation.*

import scala.xml.Elem

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
    def changePlayersNames(playersName: List[String]): GameInterface
    def getTableCard(): List[Card]
    def doDamage(player: Player): GameInterface
    def getPlayerPoints(playerNumber: Int): Double
    def isGameEnded: Boolean
    def getBestPlayerByPoints: Player
    def getWorstPlayerByPoints: Player
    def pass(): GameInterface
    def knock(): GameInterface
    def resetPasses(): GameInterface
    def swap(playersTurn: Int, idxGiveString: String, idxReceiveString: String): Try[GameInterface]
    def resetNewRound(): GameInterface
    def swapTable(playersTurn: Int, idx1: Int, idx2: Int, swapFinished: Boolean): GameInterface
    def calculateIndex(indexToGive: String): Try[Int]
    def saveGameXML(): Unit
    def loadGameXML(): Option[GameInterface]
}
