package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.util._
import de.htwg.se.thirtyone.model.gameImplementation.GameData
import de.htwg.se.thirtyone.model.gameImplementation.Player

class GameDataSwapSpec extends AnyWordSpec with Matchers {
  "GameData.swap" should {
    "return Failure on invalid receive index string" in {
      val gd = GameData(2)
      val res = gd.swap(gd.currentPlayerIndex + 1, "1", "x")
      res.isFailure shouldBe true
    }

    "return Success when receive index > 2 (no-op)" in {
      val gd = GameData(2)
      val res = gd.swap(gd.currentPlayerIndex + 1, "1", "5")
      res.isSuccess shouldBe true
      res.get shouldBe gd
    }

    "calculateIndex returns Failure for non-number" in {
      val gd = GameData(2)
      gd.calculateIndex("x").isFailure shouldBe true
    }

    "resetNewRound resets players' hasKnocked and points" in {
      val gd = GameData(2)
      val mutated = gd.copy(players = List(Player(hasKnocked = true, points = 5), Player(hasKnocked = true, points = 3)))
      val reset = mutated.resetNewRound()
      reset.players.foreach(p => p.hasKnocked shouldBe false)
      reset.players.foreach(p => p.points shouldBe 0)
    }
  }
}
