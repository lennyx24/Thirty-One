package de.htwg.se.thirtyone.model.gameImplementation

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}
import de.htwg.se.thirtyone.model.factory.*
import GameScoringStrategy.Strategy
import de.htwg.se.thirtyone.model.*

import java.nio.file.{Files, Paths}
import scala.xml.{Elem, Node, XML}

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

    override def nextPlayer(): GameInterface =
      val next: GameData =
        if currentPlayerIndex + 1 == playerCount then copy(currentPlayerIndex = 0)
        else copy(currentPlayerIndex = currentPlayerIndex + 1)
      
      val nextGameState: GameData =
        if next.players.forall(_.hasPassed) then
          val (newTable, newDrawIndex) = table.newMiddleCards(indexes, cardPositions(0), deck, drawIndex)
          next.copy(table = newTable, drawIndex = newDrawIndex).resetPasses().asInstanceOf[GameData]
        else next

      if nextGameState.currentPlayer.hasKnocked then nextGameState.copy(gameRunning = false)
      else nextGameState

    override def calculatePlayerPoints(playerNumber: Int): GameInterface =
      val cards = table.getAll(playerNumber, cardPositions).toList
      val p = scoringStrategy(cards)

      val playerIndex = playerNumber - 1
      val playerToUpdate = players(playerIndex)
      val newPlayer = playerToUpdate.copy(points = p)
      val newPlayers = players.updated(playerIndex, newPlayer)
      copy(players = newPlayers)
    
    override def getPlayersHand(): List[Card] = table.getAll(currentPlayerIndex + 1, cardPositions)

    override def getPlayersHealth(player: Int): Int = players(player).playersHealth

    override def getPlayerScore(player: Int): Double = getPlayerPoints(player)

    override def changePlayersNames(playersName: List[String]): GameInterface =
      val newPlayers = players.zip(playersName).map { case (player, newName) =>
        player.changeName(newName)
      }
      copy(players = newPlayers)

    override def getTableCard(): List[Card] = table.getAll(0, cardPositions)

    override def doDamage(player: Player): GameInterface =
      val playerIndex = players.indexOf(player)
      val newPlayer = player.receiveDamage(1)
      
      val newPlayers = players.updated(playerIndex, newPlayer)
      copy(players = newPlayers)

    override def getPlayerPoints(playerNumber: Int): Double = players(playerNumber - 1).points

    override def isGameEnded: Boolean = players.exists(!_.isAlive)

    override def getBestPlayerByPoints: Player = players.maxBy(_.points)
    
    override def getWorstPlayerByPoints: Player = players.minBy(_.points)

    override def pass(): GameInterface =
      val idx = currentPlayerIndex
      val updatedPlayer = players(idx).copy(hasPassed = true)
      val newPlayers = players.updated(idx, updatedPlayer)
      copy(players = newPlayers).nextPlayer()

    override def knock(): GameInterface =
      val newPlayer = currentPlayer.copy(hasKnocked = true)
      val newPlayers = players.updated(currentPlayerIndex, newPlayer)
      copy(players = newPlayers).resetPasses().nextPlayer()


    override def resetPasses(): GameInterface = copy(players = players.map(_.copy(hasPassed = false)))

    override def swapTable(playersTurn: Int, idx1: Int, idx2: Int, swapFinished: Boolean): GameInterface =
        val gs = copy(table = table.swap(cardPositions(playersTurn)(idx1), cardPositions(0)(idx2)))
        if swapFinished then gs.resetPasses().nextPlayer() else gs

    override def calculateIndex(indexToGive: String): Try[Int] = Try(indexToGive.toInt - 1)

    override def swap(playersTurn: Int, idxGiveString: String, idxReceiveString: String): Try[GameInterface] =
      @tailrec
      def swapRec(currentGS: GameInterface, iGiveStr: String, iReceiveStr: String): Try[GameInterface] =
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

    override def resetNewRound(): GameInterface =
      val savedPlayers = players.map(_.copy(hasKnocked = false, points = 0))
      val newGame = GameData(playerCount)
      newGame.copy(players = savedPlayers)

    override def saveGameXML(): Unit = {
      val xml: Elem =
        <GameData>
          { table.toXML }
          <strategy>{ scoringStrategy }</strategy>
          <playerCount>{playerCount}</playerCount>
          <players>{players.map(p=>p.toXML)}</players>
          <currentPlayerIndex>{currentPlayerIndex}</currentPlayerIndex>
          <deck>{deck.map(_.toXML)}</deck>
          <indexes>{indexes.mkString(",")}</indexes>
          <drawIndex>{drawIndex}</drawIndex>
          <gameRunning>{gameRunning}</gameRunning>
          <cardPositions>{
            cardPositions.zipWithIndex.map { case (posList, pi) =>
              <player index={ pi.toString }>{
                posList.map { case (r, c) => <pos row={ r.toString } col={ c.toString }/> }
                }</player>
            }
            }</cardPositions>
        </GameData>
      val userHome = System.getProperty("user.home")
      val downloadsDir = Paths.get(userHome, "Downloads")
      val name = players(0).name + players(1).name + "Game"
      val ending = ".xml"
      //falls wir mehrere mit untersch. namen haben wollen
      var target = downloadsDir.resolve(name + ending)
      var i = 1
      while (Files.exists(target)) {
        target = downloadsDir.resolve(s"${name}(${i})${ending}")
        i += 1
      }
      XML.save(target.toString, xml)
    }

object GameData:
  def apply(playerAmount: Int, gameMode: GameFactory = StandardGameFactory): GameData =
    gameMode.createGame(playerAmount)
  def loadGame(node: xml.Node): GameData =
    val tableNode = (node \ "table").head