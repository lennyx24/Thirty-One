package de.htwg.se.thirtyone.model.factory

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model._

class StandardGameFactorySpec extends AnyWordSpec with Matchers {
  "StandardGameFactory" should {
    "create a valid GameData for 2 players" in {
      val gd = StandardGameFactory.createGame(2)
      gd.playerCount shouldBe 2
      gd.deck.nonEmpty shouldBe true
      gd.table.grid.length shouldBe 3
      gd.table.grid.head.length shouldBe 9
      gd.cardPositions.length should be >= 2
      gd.players.length shouldBe 2
    }

    "create a valid GameData for 4 players" in {
      val gd = StandardGameFactory.createGame(4)
      gd.playerCount shouldBe 4
      gd.players.length shouldBe 4
      gd.cardPositions.length should be >= 4
    }
  }
}

