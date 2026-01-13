package de.htwg.se.thirtyone.model.gameImplementation

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}
import de.htwg.se.thirtyone.model.factory.*
import GameScoringStrategy.Strategy
import de.htwg.se.thirtyone.model.*
import de.htwg.se.thirtyone.model.gameImplementation.GameData.loadGame

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

    override def toXml(): Elem =
      <GameData>
        {table.toXML}
        <strategy>{GameScoringStrategy.toString(scoringStrategy)}</strategy>
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

object GameData:
  def apply(playerAmount: Int, gameMode: GameFactory = StandardGameFactory): GameData =
    gameMode.createGame(playerAmount)
  def loadGame(node: xml.Node): GameData =
    val tableNode = (node \ "table").headOption.getOrElse(throw new Exception("Saved game missing <table> node"))
    val table = Table.fromXML(tableNode)
    val scoringStrategy = GameScoringStrategy.fromString((node \ "strategy").text)
    val playerCount = (node \ "playerCount").text.toInt
    val currentPlayerIndex = (node \ "currentPlayerIndex").text.toInt
    val drawIndex = (node \ "drawIndex").text.toInt
    val gameRunning = (node \ "gameRunning").text.toBoolean
    val players: List[Player] = (node \ "players").headOption.map(p=>p.child.collect{case e: xml.Elem => e}.toList.map(Player.fromXML)).getOrElse(Nil)
    val deck: Vector[Card] = (node \ "deck").headOption.map(d => d.child.collect { case e: scala.xml.Elem => e }.toList.map(Card.fromXML).toVector).getOrElse(Vector.empty)
    val indexes: Vector[Int] = (node \ "indexes").text.split(",").flatMap(s => s.toIntOption).toVector
    val cardPositions: List[List[(Int, Int)]] =
      (node \ "cardPositions" \ "player").toList
        .sortBy(p => (p \ "@index").text.trim.toIntOption.getOrElse(0))
        .map { p =>
          (p \ "pos").toList.map { pos =>
            val r = (pos \ "@row").text.trim.toIntOption.getOrElse(0)
            val c = (pos \ "@col").text.trim.toIntOption.getOrElse(0)
            (r, c)
          }
        }
    GameData(
      table = table,
      scoringStrategy = scoringStrategy,
      playerCount = playerCount,
      players = players,
      currentPlayerIndex = currentPlayerIndex,
      deck = deck,
      indexes = indexes,
      drawIndex = drawIndex,
      gameRunning = gameRunning,
      cardPositions = cardPositions
    )