package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.util.{Success, Failure}
import de.htwg.se.thirtyone.model.gameImplementation.Card
import de.htwg.se.thirtyone.model.gameImplementation.Deck
import de.htwg.se.thirtyone.model.gameImplementation.GameData
import de.htwg.se.thirtyone.model.gameImplementation.GameScoringStrategy
import de.htwg.se.thirtyone.model.gameImplementation.Player

class GameDataSpec extends AnyWordSpec with Matchers {

  // Helper functions to create small test data
  private def mkGame(playerCount: Int = 2): GameData = GameData(playerCount)

  private def mkPlayer(
      name: String = "Nameless Player",
      hasKnocked: Boolean = false,
      points: Double = 0,
      playersHealth: Int = 2,
      isAlive: Boolean = true,
      hasPassed: Boolean = false
  ): Player = Player(name, hasKnocked, points, playersHealth, isAlive, hasPassed)

  private def mkCard(symbol: Char, value: String): Card = Card(symbol, value)

  private val defaultPositions: List[List[(Int, Int)]] = List(
    List((1, 3), (1, 4), (1, 5)), // middle
    List((0, 1), (0, 2), (0, 3)), // player1
    List((0, 5), (0, 6), (0, 7)) // player2
  )

  "A GameData" should {
    val playerCount = 2
    val game = mkGame(playerCount)

    "be initialized correctly" in {
      game.playerCount should be(playerCount)
      game.currentPlayerIndex should be(0)
      game.gameRunning should be(true)
      game.players should have size playerCount
      game.currentPlayer should be(game.players(0))
    }

    "handle player turns (nextPlayer)" in {
      val nextTurn = game.nextPlayer()
      nextTurn.currentPlayerIndex should be(1)
      
      val roundTrip = nextTurn.nextPlayer()
      roundTrip.currentPlayerIndex should be(0)
    }

    "allow a player to pass" in {
      val passedGame = game.pass()
      passedGame.currentPlayerIndex should be(1)
    }

    "allow a player to knock" in {
      val knockedGame = game.knock()
      
      knockedGame.currentPlayerIndex should be(1)
      
      knockedGame.players(0).hasKnocked should be(true)
    }

    "stop the game if the next player has already knocked" in {
      val g1 = game.knock()
      val g2 = g1.pass()
      
      g2.gameRunning should be(false)
    }

    "calculate correct indexes from strings" in {
      game.calculateIndex("1") should be(Success(0))
      game.calculateIndex("3") should be(Success(2))
    }

    "handle card swaps correctly" in {
      val singleSwap = game.swap(1, "1", "1")
      singleSwap.isSuccess should be(true)
      singleSwap.get.currentPlayerIndex should be(1) 

      val allSwap = game.swap(1, "alle", "1")
      allSwap.isSuccess should be(true)
      allSwap.get.currentPlayerIndex should be(1)
    }

    "calculate player points" in {
      val g = GameData(2)
      val gWithPoints = g.calculatePlayerPoints(1)
      gWithPoints.players(0).points should be >= 0.0
    }

    "do damage to a player" in {
      val g = GameData(2)
      val p1 = g.players(0)
      val initialHealth = p1.playersHealth
      val gDamaged = g.doDamage(p1)
      gDamaged.players(0).playersHealth should be(initialHealth - 1)
    }

    "get player points" in {
      val g = GameData(2)
      g.getPlayerPoints(1) should be(g.players(0).points)
    }

    "check if game ended" in {
      val g = GameData(2)
      g.isGameEnded should be(false)
      
      val deadPlayer = g.players(0).copy(playersHealth = 0, isAlive = false)
      val gEnded = g.copy(players = g.players.updated(0, deadPlayer))
      gEnded.isGameEnded should be(true)
    }

    "get best and worst player by points" in {
      val p1 = Player(name = "p1", hasKnocked = false, points = 10, playersHealth = 3)
      val p2 = Player(name = "p2", hasKnocked = false, points = 20, playersHealth = 3)
      val g = GameData(2).copy(players = List(p1, p2))
      
      g.getBestPlayerByPoints should be(p2)
      g.getWorstPlayerByPoints should be(p1)
    }

    "reset for new round" in {
      val p1 = mkPlayer(hasKnocked = true, points = 30, playersHealth = 3) // Knocked and has points
      val g = GameData(2).copy(players = List(p1, mkPlayer()))
      val gReset = g.resetNewRound()
      
      gReset.players(0).hasKnocked should be(false)
      gReset.players(0).points should be(0)
      gReset.gameRunning should be(true)
    }

    // Additional small, complementary tests merged here to improve coverage
    "swapTable should not advance player when swapFinished is false" in {
      val gd = GameData(2)
      val cardPositions = List(
        List((1, 3), (1, 4), (1, 5)), // middle
        List((0, 1), (0, 2), (0, 3))  // player1
      )
      val h10 = mkCard('♥', "10")
      val d4 = mkCard('♦', "4")
      val baseTable = gd.table.setAll(cardPositions(0), List(h10, d4, h10)).setAll(cardPositions(1), List(d4, h10, d4))
      val gdc = gd.copy(table = baseTable, cardPositions = cardPositions)

      val beforeIndex = gdc.currentPlayerIndex
      val posGive = cardPositions(1)(0)
      val posReceive = cardPositions(0)(0)
      val beforeGive = gdc.table.get(posGive)
      val beforeReceive = gdc.table.get(posReceive)

      val res = gdc.swapTable(1, 0, 0, false)
      res match
        case ng: GameData =>
          ng.currentPlayerIndex shouldBe beforeIndex
          ng.table.get(posGive) shouldBe beforeReceive
          ng.table.get(posReceive) shouldBe beforeGive
        case _ => fail("Expected GameData result")
    }

    "swapTable should advance player and reset passes when swapFinished is true" in {
      val gd = GameData(2)
      val players = List(mkPlayer(name = "P1", hasPassed = true), mkPlayer(name = "P2", hasPassed = true))
      val cardPositions = List(
        List((1, 3), (1, 4), (1, 5)), // middle
        List((0, 1), (0, 2), (0, 3))  // player1
      )

      val h10 = mkCard('♥', "10")
      val d4 = mkCard('♦', "4")
      val baseTable = gd.table.setAll(cardPositions(0), List(h10, d4, h10)).setAll(cardPositions(1), List(d4, h10, d4))
      val gdc = gd.copy(table = baseTable, cardPositions = cardPositions, players = players, currentPlayerIndex = 0)

      val res = gdc.swapTable(1, 0, 0, true)
      res match
        case ng: GameData =>
          ng.currentPlayerIndex shouldBe 1
          ng.players.foreach(p => p.hasPassed shouldBe false)
        case _ => fail("Expected GameData result")
    }

    "changePlayersNames should update player names" in {
      val gd = GameData(2)
      val names = List("Alice", "Bob")
      val ng = gd.changePlayersNames(names)
      ng.players(0).name shouldBe "Alice"
      ng.players(1).name shouldBe "Bob"
    }

    "calculatePlayerPoints with simpleScoringStrategy should sum points" in {
      val gd = GameData(2)
      val cardPositions = defaultPositions
      val h10 = mkCard('♥', "10")
      val d4 = mkCard('♦', "4")
      val s7 = mkCard('♠', "7")
      // set player1 hand to 10,4,7
      val baseTable = gd.table.setAll(cardPositions(1), List(h10, d4, s7))
      val gdc = gd.copy(table = baseTable, cardPositions = cardPositions, scoringStrategy = GameScoringStrategy.simpleScoringStrategy)

      val res = gdc.calculatePlayerPoints(1)
      res.players(0).points should be(10.0 + 4.0 + 7.0)
    }

    "calculatePlayerPoints with advancedScoringStrategy should calculate points considering rounds" in {
      val gd = GameData(2)
      val cardPositions = defaultPositions
      // create three-of-a-kind for player1 (value "7") to trigger 30.5
      val c1 = mkCard('♥', "7")
      val c2 = mkCard('♦', "7")
      val c3 = mkCard('♠', "7")
      val baseTable = gd.table.setAll(cardPositions(1), List(c1, c2, c3))
      val gdc = gd.copy(table = baseTable, cardPositions = cardPositions, scoringStrategy = GameScoringStrategy.normalScoringStrategy)

      val res = gdc.calculatePlayerPoints(1)
      res.players(0).points should be(30.5)
    }

    // Additional lightweight coverage tests
    "calculateIndex should return Failure for non-number strings" in {
      val gd = GameData(2)
      val res = gd.calculateIndex("notANumber")
      res.isFailure shouldBe true
    }

    "swap should return Success when receive index > 2 (no-op)" in {
      val gd = GameData(2)
      // try to swap with receive index 4 (meaning indexReceive = 3 > 2)
      val res = gd.swap(1, "1", "4")
      res.isSuccess shouldBe true
    }

    "nextPlayer should reload middle cards and reset passes when all players have passed" in {
      val deck = Deck().deck
      val indexes = (0 until deck.size).toVector
      val positions = defaultPositions
      // create a game where both players have passed
      val players = List(mkPlayer(hasPassed = true), mkPlayer(hasPassed = true))
      val gd = GameData(2).copy(players = players, cardPositions = positions, indexes = indexes, drawIndex = 0)

      val next = gd.nextPlayer().asInstanceOf[GameData]
      // since both had passed, nextPlayer should have called newMiddleCards and reset passes
      next.drawIndex should be >= 3
      next.players.foreach(p => p.hasPassed shouldBe false)
    }

  }
}
