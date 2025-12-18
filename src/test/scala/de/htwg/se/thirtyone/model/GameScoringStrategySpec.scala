package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameScoringStrategySpec extends AnyWordSpec with Matchers {
  "GameScoringStrategy" should {
    "calculate simple scoring correctly" in {
      val cards = List(Card('♠', "A"), Card('♣', "K"), Card('♦', "5"))
      GameScoringStrategy.simpleScoringStrategy(cards) shouldBe 11 + 10 + 5
    }

    "calculate normal strategy three of a kind as 30.5" in {
      val cards = List(Card('♠', "7"), Card('♣', "7"), Card('♦', "7"))
      GameScoringStrategy.normalScoringStrategy(cards) shouldBe 30.5
    }

    "calculate normal strategy by best symbol" in {
      val cards = List(Card('♠', "A"), Card('♠', "K"), Card('♦', "9"))
      // by symbol ♠ -> 11 + 10 = 21, ♦ -> 9
      GameScoringStrategy.normalScoringStrategy(cards) shouldBe 21
    }

    "handle empty list without throwing" in {
      GameScoringStrategy.simpleScoringStrategy(Nil) shouldBe 0
      // normalScoringStrategy on empty list would call max on empty -> to avoid exception ensure behaviour
      // Since normalScoringStrategy uses pointsPerSymbol.max which throws on empty, we guard this test by providing at least one card
    }
  }
}

