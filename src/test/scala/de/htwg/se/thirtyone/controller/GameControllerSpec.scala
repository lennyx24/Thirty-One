 package de.htwg.se.thirtyone.controller

 import org.scalatest.wordspec.AnyWordSpec
 import org.scalatest.matchers.should.Matchers.*

 import de.htwg.se.thirtyone.model._

 class GameControllerSpec extends AnyWordSpec {
   "GameController" should {
     "be able to pass" in {
       val playersTurn = 1
       val dummyState = GameState(Table(), 3, playersTurn, Deck(), false, Nil)
       val gameController = GameController(dummyState)
       gameController.pass(playersTurn)
       gameController.gameState.currentPlayer should be (2)
       val playersTurn2 = 2
       gameController.pass(playersTurn2)
       gameController.gameState.currentPlayer should be (3)
       val playersTurn3 = 3
       gameController.pass(playersTurn3)
       gameController.gameState.currentPlayer should be(1)
     }
     "be able to knock" in {
       val playersTurn = 1
       val dummyState = GameState(Table(), 3, playersTurn, Deck(), false, Nil)
       val gameController = GameController(dummyState)
       gameController.knock(playersTurn)
       gameController.gameState.currentPlayer should be(2)
       val playersTurn2 = 2
       gameController.knock(playersTurn2)
       gameController.gameState.currentPlayer should be(3)
       val playersTurn3 = 3
       gameController.knock(playersTurn3)
       gameController.gameState.currentPlayer should be(1)
     }
     "be able to calculate index" in {
       val dummyState = GameState(Table(), 3, 1, Deck(), false, Nil)
       val indexToGive = "1"
       GameController(dummyState).calculateIndex(indexToGive) should be (0)
       val indexToGive2 = "2"
       GameController(dummyState).calculateIndex(indexToGive2) should be(1)
       val indexToGive3 = "3"
       GameController(dummyState).calculateIndex(indexToGive3) should be(2)
     }
     "be able to swap" in {
       val cardPositions = List(
         List((1, 3), (1, 4), (1, 5)), //Position Middle Cards
         List((0, 1), (0, 2), (0, 3)), //Position Player 1
         List((0, 5), (0, 6), (0, 7)), //Position Player 2
         List((2, 5), (2, 6), (2, 7)), //Position Player 3
         List((2, 1), (2, 2), (2, 3)), //Position Player 4
       )
       val playersTurn = 1
       val deck = Deck()
       val indexes = (0 until deck.deck.size).toVector
       val table = Table().createGameTable(4, indexes, cardPositions, deck)
       val dummyState = GameState(table, 4, playersTurn, deck, true, cardPositions)
       val gameController = GameController(dummyState)

       gameController.swap(playersTurn, "1", "1")
       gameController.gameState.table should not be (table) //table geändert
       gameController.pass(gameController.gameState.currentPlayer)  //3x passen, dass wieder spieler 1 ist
       gameController.pass(gameController.gameState.currentPlayer)
       gameController.pass(gameController.gameState.currentPlayer)
       gameController.swap(gameController.gameState.currentPlayer, "1", "1")
       gameController.gameState.table should be (table)   //table wieder wie am anfang

       gameController.swap(gameController.gameState.currentPlayer, "alle", "1")
       gameController.gameState.table should not be (table) //table geändert
       gameController.pass(gameController.gameState.currentPlayer) //3x passen, dass wieder spieler 1 ist
       gameController.pass(gameController.gameState.currentPlayer)
       gameController.pass(gameController.gameState.currentPlayer)
       gameController.swap(gameController.gameState.currentPlayer, "alle", "1")
       gameController.gameState.table should be (table)   //table wieder wie am anfang
     }
     "be able to initialize a game" in {
       val playersCount = 4
       val dummyState = GameState(Table(), playersCount, 1, Deck(), false, Nil)
       val gameController = GameController(dummyState)
       gameController.initializeGame(playersCount)
       gameController.gameState.table should not be(Nil)
     }
   }
 }
