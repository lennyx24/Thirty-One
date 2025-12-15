package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.util.{Success, Failure}

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
      game.calculateIndex("1") should be(Success(0))
      game.calculateIndex("3") should be(Success(2))
    }

    "handle card swaps correctly" in {
      val singleSwap = game.swap(game, 1, "1", "1")
      singleSwap.isSuccess should be(true)
      singleSwap.get.currentPlayerIndex should be(1) 

      val allSwap = game.swap(game, 1, "alle", "1")
      allSwap.isSuccess should be(true)
      allSwap.get.currentPlayerIndex should be(1)
    }
  }
}