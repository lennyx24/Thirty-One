package de.htwg.se.thirtyone.model.factory

import de.htwg.se.thirtyone.model.game.{Deck, GameData, GameScoringStrategy, Player, Table}

object StandardGameFactory extends GameFactory:
  override def createGame(playerCount: Int): GameData =
    val cardPositions = List(
      List((1, 3), (1, 4), (1, 5)), // Position Middle Cards
      List((0, 1), (0, 2), (0, 3)), // Position Player 1
      List((0, 5), (0, 6), (0, 7)), // Position Player 2
      List((2, 5), (2, 6), (2, 7)), // Position Player 3
      List((2, 1), (2, 2), (2, 3)), // Position Player 4
    )
    val deck = Deck().deck
    val table = Table()
    val indexes = table.indexes(deck)
    val (gameTable, drawIndex) = table.createGameTable(playerCount, indexes, cardPositions, deck)
    val players = List.fill(playerCount)(Player())

    GameData(
      table = gameTable,
      scoringStrategy = GameScoringStrategy.normalScoringStrategy,
      playerCount = playerCount,
      players = players,
      currentPlayerIndex = 0,
      deck = deck,
      indexes = indexes,
      drawIndex = drawIndex,
      gameRunning = true,
      cardPositions = cardPositions
    )
