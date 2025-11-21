package de.htwg.se.thirtyone.model

case class GameState(
    table: Table,
    playerCount: Int,
    currentPlayer: Int,
    deck: Deck,
    gameRunning: Boolean,
    cardPositions: List[List[(Int, Int)]]
):
    def nextPlayer(): Int = if currentPlayer >= playerCount then 1 else currentPlayer + 1

    def pass(playersTurn: Int): GameState = copy(currentPlayer = nextPlayer())
    
    def knock(playersTurn: Int): GameState = copy(currentPlayer = nextPlayer())
    
    def swap(playersTurn: Int, idx1: Int, idx2: Int, swapFinished: Boolean): GameState =
        val gs = copy(table = table.swap(cardPositions(playersTurn)(idx1), cardPositions(0)(idx2)))
        if swapFinished then gs.copy(currentPlayer = nextPlayer()) else gs

object GameState:
    def apply(playerCount: Int): GameState =
        val positions = List(
            List((1, 3), (1, 4), (1, 5)), //Position Middle Cards
            List((0, 1), (0, 2), (0, 3)), //Position Player 1
            List((0, 5), (0, 6), (0, 7)), //Position Player 2
            List((2, 5), (2, 6), (2, 7)), //Position Player 3
            List((2, 1), (2, 2), (2, 3)), //Position Player 4
        )
        val cardDeck = Deck()
        val indexes = Table().indexes(cardDeck)
        val gameTable = Table().createGameTable(playerCount, indexes, positions, cardDeck)

        GameState(
        table = gameTable,
        playerCount = playerCount,
        currentPlayer = 1,
        deck = cardDeck,
        gameRunning = true,
        cardPositions = positions
    )