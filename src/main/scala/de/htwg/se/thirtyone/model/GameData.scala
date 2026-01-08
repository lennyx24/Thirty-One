package de.htwg.se.thirtyone.model

import scala.annotation.tailrec
import scala.util.{Try, Success, Failure}
import de.htwg.se.thirtyone.model.factory._
import de.htwg.se.thirtyone.model.GameScoringStrategy.Strategy

case class GameData(
    table: Table,
    scoringStrategy: Strategy,
    playerCount: Int,
    players: List[Player],
    currentPlayerIndex: Int,
    deck: Vector[Card],
    indexes: Vector[Int],
    drawIndex: Int,
    gameRunning: Boolean,
    cardPositions: List[List[(Int, Int)]]
) extends GameInterface:
    override def currentPlayer: Player = players(currentPlayerIndex)

    override def nextPlayer(): GameData =
      val nextGameState =
        if currentPlayerIndex + 1 == playerCount then copy(currentPlayerIndex = 0)
        else copy(currentPlayerIndex = currentPlayerIndex + 1)
      }
      val nextGameState =
        if next.players.forall(_.hasPassed) then
          val (newTable, newDrawIndex) = table.newMiddleCards(indexes, cardPositions(0), deck, drawIndex)
          next.copy(table = newTable, drawIndex = newDrawIndex).resetPasses()
        else next

      if nextGameState.currentPlayer.hasKnocked then copy(gameRunning = false)
      else nextGameState

    override def calculatePlayerPoints(playerNumber: Int): GameData =
      val cards = table.getAll(playerNumber).toList
      val p = scoringStrategy(cards)

      val playerIndex = playerNumber - 1
      val playerToUpdate = players(playerIndex)
      val newPlayer = playerToUpdate.copy(points = p)
      val newPlayers = players.updated(playerIndex, newPlayer)
      copy(players = newPlayers)
    
    override def getPlayersHand(): List[Card] = table.getAll(currentPlayerIndex + 1)

    override def getPlayersHealth(player: Int): Int = players(player).playersHealth

    override def getPlayerScore(player: Int): Double = getPlayerPoints(player)

    override def getTableCard(): List[Card] = table.getAll(0)
    
    override def doDamage(player: Player): GameData =
      val playerIndex = players.indexOf(player)
      val newPlayer = player.receiveDamage(1)
      
      val newPlayers = players.updated(playerIndex, newPlayer)
      copy(players = newPlayers)

    override def getPlayerPoints(playerNumber: Int): Double = players(playerNumber - 1).points

    override def isGameEnded: Boolean = players.exists(!_.isAlive)

    override def getBestPlayerByPoints: Player = players.maxBy(_.points)
    
    override def getWorstPlayerByPoints: Player = players.minBy(_.points)

    def pass(): GameData = {
      val idx = currentPlayerIndex
      val updatedPlayer = players(idx).copy(hasPassed = true)
      val newPlayers = players.updated(idx, updatedPlayer)
      copy(players = newPlayers).nextPlayer()
    }

    def knock(): GameData =
      val newPlayer = currentPlayer().copy(hasKnocked = true)
      val newPlayers = players.updated(currentPlayerIndex, newPlayer)
      copy(players = newPlayers).resetPasses().nextPlayer()


    def resetPasses(): GameData =
      copy(players = players.map(_.copy(hasPassed = false)))

    private def swapTable(playersTurn: Int, idx1: Int, idx2: Int, swapFinished: Boolean): GameData =
        val gs = copy(table = table.swap(cardPositions(playersTurn)(idx1), cardPositions(0)(idx2)))
        if swapFinished then gs.resetPasses().nextPlayer() else gs

    private def calculateIndex(indexToGive: String): Try[Int] = Try(indexToGive.toInt - 1)

    override def swap(playersTurn: Int, idxGiveString: String, idxReceiveString: String): Try[GameData] = 
      @tailrec
      def swapRec(currentGS: GameData, iGiveStr: String, iReceiveStr: String): Try[GameData] =
        calculateIndex(iReceiveStr) match
          case Failure(e) => Failure(e)
          case Success(indexReceive) =>
            if indexReceive > 2 then Success(currentGS)
            else
              iGiveStr match
                case "1" | "2" | "3" =>
                  calculateIndex(iGiveStr) match
                    case Failure(e) => Failure(e)
                    case Success(indexGive) =>
                      val nextGS = currentGS.swapTable(playersTurn, indexGive, indexReceive, true)
                      swapRec(nextGS, iGiveStr, "4") // Rekursion beenden
                case "alle" =>
                  val nextGS =
                    if indexReceive > 1 then currentGS.swapTable(playersTurn, indexReceive, indexReceive, true)
                    else currentGS.swapTable(playersTurn, indexReceive, indexReceive, false)
                  val nextIndex = (indexReceive + 2).toString
                  swapRec(nextGS, iGiveStr, nextIndex)
                case _ => Failure(new Exception("Invalid give string"))

      swapRec(this, idxGiveString, idxReceiveString)

    override def resetNewRound(): GameData =
      val savedPlayers = players.map(_.copy(hasKnocked = false, points = 0))
      val newGame = GameData(playerCount)
      newGame.copy(players = savedPlayers)

object GameData:
    def apply(playerAmount: Int, gameMode: GameFactory = StandardGameFactory): GameData =
      gameMode.createGame(playerAmount)
        