package de.htwg.se.thirtyone.model.factory

import de.htwg.se.thirtyone.model._

object StandardGameFactory extends GameFactory:
    override def createGame(playerAmount: Int): GameData =
        val positions = List(
            List((1, 3), (1, 4), (1, 5)), //Position Middle Cards
            List((0, 1), (0, 2), (0, 3)), //Position Player 1
            List((0, 5), (0, 6), (0, 7)), //Position Player 2
            List((2, 5), (2, 6), (2, 7)), //Position Player 3
            List((2, 1), (2, 2), (2, 3)), //Position Player 4
        )
        // Create normal deck with 52 Cards
        val cardDeck = Deck().deck
        val indexes = Table().indexes(cardDeck)
        val gameTable = Table().createGameTable(playerAmount, indexes, positions, cardDeck)

        // Create small deck with 32 Card
        val smallCardDeck = Deck().smallDeck
        val indexesSmall = Table().indexes(smallCardDeck)
        val gameTableSmall = Table().createGameTable(playerAmount, indexesSmall, positions, smallCardDeck)

        val playersList = (1 to playerAmount).map(i => Player()).toList

        GameData(
        table = gameTableSmall,
        scoringStrategy = GameScoringStrategy.normalScoringStrategy,
        playerCount = playerAmount,
        players = playersList,
        currentPlayerIndex = 0,
        deck = smallCardDeck,
        gameRunning = true,
        cardPositions = positions
        )
