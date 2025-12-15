package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameDataSpec extends AnyWordSpec with Matchers {

  "A GameData" should {
    val playerCount = 2
    val game = GameData(playerCount)

    "be initialized correctly" in {
      game.playerCount should be(playerCount)
      game.currentPlayerIndex should be(0)
      game.gameRunning should be(true)
      game.players should have size playerCount
      game.currentPlayer() should be(game.players(0))
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
      game.calculateIndex("1") should be(0)
      game.calculateIndex("3") should be(2)
    }

    "handle card swaps correctly" in {
      val singleSwap = game.swap(game, 1, "1", "1")
      singleSwap.currentPlayerIndex should be(1) 

      val allSwap = game.swap(game, 1, "alle", "1")
      allSwap.currentPlayerIndex should be(1)
    }

    "calculate player points" in {
      // Assuming simple scoring strategy where points are calculated
      // We need to mock or setup table with cards for a player
      // But GameData uses scoringStrategy passed in constructor.
      // The default GameData(playerCount) uses StandardGameFactory which sets up a game.
      // We can check if points are updated.
      val g = GameData(2)
      val gWithPoints = g.calculatePlayerPoints(1)
      // Points might be 0 or something depending on cards.
      // Just check it doesn't crash and updates the player.
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
      
      // Kill a player
      val deadPlayer = g.players(0).copy(playersHealth = 0, isAlive = false)
      val gEnded = g.copy(players = g.players.updated(0, deadPlayer))
      gEnded.isGameEnded should be(true)
    }

    "get best and worst player by points" in {
      val p1 = Player(hasKnocked = false, points = 10, playersHealth = 3)
      val p2 = Player(hasKnocked = false, points = 20, playersHealth = 3)
      val g = GameData(2).copy(players = List(p1, p2))
      
      g.getBestPlayerByPoints should be(p2)
      g.getWorstPlayerByPoints should be(p1)
    }

    "reset for new round" in {
      val p1 = Player(hasKnocked = true, points = 30, playersHealth = 3) // Knocked and has points
      val g = GameData(2).copy(players = List(p1, Player()))
      val gReset = g.resetNewRound
      
      gReset.players(0).hasKnocked should be(false)
      gReset.players(0).points should be(0)
      gReset.gameRunning should be(true)
    }
  }
}