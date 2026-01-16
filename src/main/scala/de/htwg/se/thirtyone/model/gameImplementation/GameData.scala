package de.htwg.se.thirtyone.model.gameImplementation

import de.htwg.se.thirtyone.model.*
import de.htwg.se.thirtyone.model.factory.*
import de.htwg.se.thirtyone.model.gameImplementation.GameScoringStrategy.Strategy
import play.api.libs.json.{JsValue, Json}

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, Node}

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

  private def indexOfPlayer(player: Player): Option[Int] =
    val idx = players.indexWhere(p => p.id == player.id)
    if idx >= 0 then Some(idx) else None

  private def positionIndex(player: Player): Option[Int] =
    indexOfPlayer(player).map(_ + 1)

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

  override def calculatePlayerPoints(player: Player): GameInterface =
    positionIndex(player) match
      case None => this
      case Some(posIndex) =>
        val cards = table.getAll(posIndex, cardPositions).toList
        val p = scoringStrategy(cards)
        val playerIndex = posIndex - 1
        val playerToUpdate = players(playerIndex)
        val newPlayer = playerToUpdate.copy(points = p)
        val newPlayers = players.updated(playerIndex, newPlayer)
        copy(players = newPlayers)

  override def getPlayersHand(): List[Card] = table.getAll(currentPlayerIndex + 1, cardPositions)

  override def getPlayersHealth(player: Player): Int =
    indexOfPlayer(player).map(players(_).playersHealth).getOrElse(0)

  override def getPlayerScore(player: Player): Double = getPlayerPoints(player)

  override def changePlayerName(player: Player, newName: String): GameInterface =
    indexOfPlayer(player) match
      case None => this
      case Some(playerIdx) =>
        val newPlayer = players(playerIdx).changeName(newName)
        val newPlayers = players.updated(playerIdx, newPlayer)
        copy(players = newPlayers)

  override def changePlayersNames(playersName: List[String]): GameInterface =
    val newPlayers = players.zip(playersName).map { case (player, newName) =>
      player.changeName(newName)
    }
    copy(players = newPlayers)

  override def getTableCard(): List[Card] = table.getAll(0, cardPositions)

  override def doDamage(player: Player): GameInterface =
    indexOfPlayer(player) match
      case None => this
      case Some(playerIndex) =>
        val newPlayer = player.receiveDamage(1)
        val newPlayers = players.updated(playerIndex, newPlayer)
        copy(players = newPlayers)

  override def getPlayerPoints(player: Player): Double =
    indexOfPlayer(player).map(players(_).points).getOrElse(0.0)

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

  override def swapTable(player: Player, idx1: Int, idx2: Int, swapFinished: Boolean): GameInterface =
    positionIndex(player) match
      case None => this
      case Some(playersTurn) =>
        val gs = copy(table = table.swap(cardPositions(playersTurn)(idx1), cardPositions(0)(idx2)))
        if swapFinished then gs.resetPasses().nextPlayer() else gs

  override def calculateIndex(indexToGive: String): Try[Int] = Try(indexToGive.toInt - 1)

  override def swap(player: Player, idxGiveString: String, idxReceiveString: String): Try[GameInterface] =
    positionIndex(player) match
      case None => Failure(new Exception("Unknown player"))
      case Some(_) =>
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
                        val nextGS = currentGS.swapTable(player, indexGive, indexReceive, true)
                        swapRec(nextGS, iGiveStr, "4") // Rekursion beenden
                  case "alle" =>
                    val nextGS =
                      if indexReceive > 1 then currentGS.swapTable(player, indexReceive, indexReceive, true)
                      else currentGS.swapTable(player, indexReceive, indexReceive, false)
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
      {table.toXml}
      <strategy>{GameScoringStrategy.toString(scoringStrategy)}</strategy>
      <playerCount>{playerCount}</playerCount>
      <players>{players.map(p=>p.toXml)}</players>
      <currentPlayerIndex>{currentPlayerIndex}</currentPlayerIndex>
      <deck>{deck.map(_.toXml)}</deck>
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

  override def toJson(): JsValue =
    Json.obj(
      "GameData" -> Json.obj(
        "table" -> table.toJson,
        "strategy" -> GameScoringStrategy.toString(scoringStrategy),
        "playerCount" -> playerCount,
        "players" -> Json.toJson(players.map(_.toJson)),
        "currentPlayerIndex" -> currentPlayerIndex,
        "deck" -> Json.toJson(deck.map(_.toJson)),
        "indexes" -> Json.toJson(indexes),
        "drawIndex" -> drawIndex,
        "gameRunning" -> gameRunning,
        "cardPositions" -> Json.toJson(
          cardPositions.map { posList =>
            Json.toJson(posList.map { case (r, c) => Json.obj("row" -> r, "col" -> c) })
          }
        )
      )
    )

object GameData:
  def apply(playerAmount: Int, gameMode: GameFactory = StandardGameFactory): GameData =
    gameMode.createGame(playerAmount)

  def loadGame(node: xml.Node): GameData =
    val tableNode = (node \ "table").headOption.getOrElse(throw new Exception("Saved game missing <table> node"))
    val table = Table.fromXml(tableNode)
    val scoringStrategy = GameScoringStrategy.fromString((node \ "strategy").text)
    val playerCount = (node \ "playerCount").text.toInt
    val currentPlayerIndex = (node \ "currentPlayerIndex").text.toInt
    val drawIndex = (node \ "drawIndex").text.toInt
    val gameRunning = (node \ "gameRunning").text.toBoolean
    val players: List[Player] = (node \ "players").headOption.map(p => p.child.collect { case e: xml.Elem => e }.toList.map(Player.fromXml)).getOrElse(Nil)
    val deck: Vector[Card] = (node \ "deck").headOption.map(d => d.child.collect { case e: scala.xml.Elem => e }.toList.map(Card.fromXml).toVector).getOrElse(Vector.empty)
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

  def loadGame(js: JsValue): GameData =
    val node = (js \ "GameData").toOption.getOrElse(js)
    val tableNode = (node \ "table").toOption.getOrElse(node)
    val table = Table.fromJson(tableNode)
    val scoringStrategy = GameScoringStrategy.fromString((node \ "strategy").asOpt[String].getOrElse("STANDARD"))
    val playerCount = (node \ "playerCount").asOpt[Int].getOrElse(0)
    val currentPlayerIndex = (node \ "currentPlayerIndex").asOpt[Int].getOrElse(0)
    val drawIndex = (node \ "drawIndex").asOpt[Int].getOrElse(0)
    val gameRunning = (node \ "gameRunning").asOpt[Boolean].getOrElse(false)
    val players = (node \ "players").asOpt[Seq[JsValue]].getOrElse(Seq.empty).toList.map(pj => Player.fromJson(pj))
    val deck = (node \ "deck").asOpt[Seq[JsValue]].getOrElse(Seq.empty).toVector.map(cj => Card.fromJson(cj))
    val indexes: Vector[Int] =
      (node \ "indexes").asOpt[Seq[Int]].map(_.toVector)
        .orElse((node \ "indexes").asOpt[String].map(s => s.split(",").flatMap(_.toIntOption).toVector))
        .getOrElse(Vector.empty)
    val cpSeq = (node \ "cardPositions").asOpt[Seq[Seq[JsValue]]].getOrElse(Seq.empty)
    val cardPositions: List[List[(Int, Int)]] =
      cpSeq.map { rowSeq =>
        rowSeq.map { cellJs =>
          val r = (cellJs \ "row").asOpt[Int].getOrElse(0)
          val c = (cellJs \ "col").asOpt[Int].getOrElse(0)
          (r, c)
        }.toList
      }.toList
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