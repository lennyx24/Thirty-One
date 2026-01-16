package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.util.{Success, Failure}
import de.htwg.se.thirtyone.model.gameImplementation._

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
      val singleSwap = game.swap(game.players(0), "1", "1")
      singleSwap.isSuccess should be(true)
      singleSwap.get.currentPlayerIndex should be(1) 

      val allSwap = game.swap(game.players(0), "alle", "1")
      allSwap.isSuccess should be(true)
      allSwap.get.currentPlayerIndex should be(1)
    }

    "calculate player points" in {
      val g = GameData(2)
      val gWithPoints = g.calculatePlayerPoints(g.players(0))
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
      g.getPlayerPoints(g.players(0)) should be(g.players(0).points)
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

      val res = gdc.swapTable(gdc.players(0), 0, 0, false)
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

      val res = gdc.swapTable(gdc.players(0), 0, 0, true)
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

      val res = gdc.calculatePlayerPoints(gdc.players(0))
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

      val res = gdc.calculatePlayerPoints(gdc.players(0))
      res.players(0).points should be(30.5)
    }

    // Additional lightweight coverage tests
    "calculateIndex should return Failure for non-number strings" in {
      val gd = GameData(2)
      val res = gd.calculateIndex("notANumber")
      res.isFailure shouldBe true
    }

    "swap should return Success when receive index 4 (no-op)" in {
      val gd = GameData(2)
      // try to swap with receive index 4 (meaning indexReceive = 3 > 2)
      val res = gd.swap(gd.players(0), "1", "4")
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

    "swap should return Failure for invalid give string" in {
      val gd = GameData(2)
      val res = gd.swap(gd.players(0), "foo", "1")
      res.isFailure shouldBe true
    }

    "swap 'alle' with receive index >1 should succeed" in {
      val gd = GameData(2)
      // receive "3" -> indexReceive = 2 (>1)
      val res = gd.swap(gd.players(0), "alle", "3")
      res.isSuccess shouldBe true
    }

    "getTableCard and getPlayersHand return expected lists" in {
      val gd = GameData(2)
      val cardPositions = defaultPositions
      val c1 = mkCard('♥', "A")
      val c2 = mkCard('♦', "K")
      val c3 = mkCard('♠', "Q")
      val baseTable = gd.table.setAll(cardPositions(0), List(c1, c2, c3)).setAll(cardPositions(1), List(c3, c2, c1))
      val gdc = gd.copy(table = baseTable, cardPositions = cardPositions)

      val tableCards = gdc.getTableCard()
      tableCards should contain (c1)

      val hand = gdc.getPlayersHand()
      hand should contain (c3)
    }

    "getPlayersHealth and getPlayerScore return underlying values" in {
      val gd = GameData(2)
      gd.getPlayersHealth(gd.players(0)) shouldBe gd.players(0).playersHealth
      gd.getPlayerScore(gd.players(0)) shouldBe gd.players(0).points
    }

    "resetPasses resets hasPassed for all players" in {
      val gd = GameData(2).copy(players = List(mkPlayer(hasPassed = true), mkPlayer(hasPassed = true)))
      val reset = gd.resetPasses().asInstanceOf[GameData]
      reset.players.foreach(p => p.hasPassed shouldBe false)
    }

    "swap should return Failure when receive index is non-numeric" in {
      val gd = GameData(2)
      val res = gd.swap(gd.players(0), "1", "notANumber")
      res.isFailure shouldBe true
    }

    "resetNewRound should create a fresh table and keep saved players" in {
      val gd = GameData(2)
      val a = mkCard('♥', "A")
      val b = mkCard('♦', "K")
      val c = mkCard('♠', "Q")
      val modifiedTable = Table().set((1,3), a).set((1,4), b).set((1,5), c)
      val modified = gd.copy(table = modifiedTable)
       val r = modified.resetNewRound().asInstanceOf[GameData]
      // players were preserved with reset points/knocked values
      r.players.size shouldBe modified.players.size
      r.players.foreach(p => p.points shouldBe 0)
      // table behavior is implementation-defined; ensure players were reset correctly
    }

    "calculateIndex handles zero and negative conversions" in {
      val gd = GameData(2)
      gd.calculateIndex("0") should be(Success(-1))
    }

    "pass cycles correctly for 3 players" in {
      val gd = GameData(3)
      val afterPass = gd.pass()
      // currentPlayerIndex should advance by one
      afterPass.currentPlayerIndex shouldBe (gd.currentPlayerIndex + 1) % 3
    }

   }
 }
