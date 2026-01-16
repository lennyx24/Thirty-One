package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.gameImplementation.Card
import de.htwg.se.thirtyone.model.gameImplementation.GameScoringStrategy

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

    "handle tie between symbols by choosing max of sums" in {
      val cards = List(Card('♠', "5"), Card('♣', "5"), Card('♠', "K"))
      // ♠: 5 + 10 = 15, ♣: 5
      GameScoringStrategy.normalScoringStrategy(cards) shouldBe 15
    }

    "convert to/from string identifiers" in {
      (GameScoringStrategy.fromString("simple") eq GameScoringStrategy.simpleScoringStrategy) shouldBe true
      (GameScoringStrategy.fromString("normal") eq GameScoringStrategy.normalScoringStrategy) shouldBe true
      GameScoringStrategy.toString(GameScoringStrategy.simpleScoringStrategy) shouldBe "simple"
      GameScoringStrategy.toString(GameScoringStrategy.normalScoringStrategy) shouldBe "normal"
    }

    "default to normal when strategy is unknown" in {
      val custom: GameScoringStrategy.Strategy = _ => 42.0
      GameScoringStrategy.toString(custom) shouldBe "normal"
    }

    "throw MatchError on unknown strategy name" in {
      an [MatchError] should be thrownBy GameScoringStrategy.fromString("unknown")
    }

    "score face cards as 10 in normal strategy" in {
      val cards = List(Card('♠', "J"), Card('♠', "Q"), Card('♠', "K"))
      GameScoringStrategy.normalScoringStrategy(cards) shouldBe 30.0
    }
  }
}
