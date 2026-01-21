package de.htwg.se.thirtyone.model

import de.htwg.se.thirtyone.model.game.*
import play.api.libs.json.JsValue

import scala.util.Try
import scala.xml.Elem

trait GameInterface:
  def table: Table
  def playerCount: Int
  def players: List[Player]
  def currentPlayerIndex: Int
  def gameRunning: Boolean
  def cardPositions: List[List[(Int, Int)]]
  def playerPositions(player: Player): List[(Int, Int)]
  def currentPlayer: Player
  def nextPlayer(): GameInterface
  def calculatePlayerPoints(player: Player): GameInterface
  def getPlayersHand(): List[Card]
  def getPlayersHealth(player: Player): Int
  def getPlayerScore(player: Player): Double
  def changePlayerName(player: Player, newName: String): GameInterface
  def changePlayersNames(playersName: List[String]): GameInterface
  def getTableCard(): List[Card]
  def doDamage(player: Player): GameInterface
  def getPlayerPoints(player: Player): Double
  def isGameEnded: Boolean
  def getBestPlayerByPoints: Player
  def getWorstPlayerByPoints: Player
  def pass(): GameInterface
  def knock(): GameInterface
  def resetPasses(): GameInterface
  def swap(player: Player, idxGiveString: String, idxReceiveString: String): Try[GameInterface]
  def resetNewRound(): GameInterface
  def swapTable(player: Player, idx1: Int, idx2: Int, swapFinished: Boolean): GameInterface
  def calculateIndex(indexToGive: String): Try[Int]
  def toXml(): Elem
  def toJson(): JsValue
