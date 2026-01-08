package de.htwg.se.thirtyone.model.factory

import de.htwg.se.thirtyone.model.*

object StandardGameFactory extends GameFactory:
  override def createGame(playerAmount: Int): GameData =
    val positions = List(
      List((1, 3), (1, 4), (1, 5)), //Position Middle Cards
      List((0, 1), (0, 2), (0, 3)), //Position Player 1
      List((0, 5), (0, 6), (0, 7)), //Position Player 2
      List((2, 5), (2, 6), (2, 7)), //Position Player 3
      List((2, 1), (2, 2), (2, 3)), //Position Player 4
    )
    val modus = "normal"
    val cardDeck = modus match {
      case "normal" => Deck().deck
      case "small" => Deck().smallDeck
      case _ => Deck().deck
    }
    val indexes = Table().indexes(cardDeck)
    val (gameTable, drawIndex) = Table().createGameTable(playerAmount, indexes, positions, cardDeck)

    val playersList = (1 to playerAmount).map(i => Player()).toList

    GameData(
      table = gameTable,
      scoringStrategy = GameScoringStrategy.normalScoringStrategy,
      playerCount = playerAmount,
      players = playersList,
      currentPlayerIndex = 0,
      deck = cardDeck,
      indexes = indexes,
      drawIndex = drawIndex,
      gameRunning = true,
      cardPositions = positions
    )
