package de.htwg.se.thirtyone.model.gameImplementation

object GameScoringStrategy:
  type Strategy = List[Card] => Double

  private def cardPoints(card: Card): Double =
    card.value match
      case "A" => 11
      case "K" | "Q" | "J" => 10
      case _ => card.value.toDouble

  val simpleScoringStrategy: Strategy = (cards: List[Card]) =>
    cards.map(cardPoints).sum

  val normalScoringStrategy: Strategy = (cards: List[Card]) =>
    val bySymbol = cards.groupBy(_.symbol)
    val pointsPerSymbol = bySymbol.map {
      case (_, symbolCards) => symbolCards.map(cardPoints).sum
    }

    val byValue = cards.groupBy(_.value)
    val threeOfAKind = if byValue.size == 1 then 30.5 else 0.0

    Math.max(pointsPerSymbol.max, threeOfAKind)

  def fromString (name: String): Strategy =
    name match {
      case "simple" => simpleScoringStrategy
      case "normal" => normalScoringStrategy
    }
  def toString (strategy: Strategy): String =
    if strategy eq simpleScoringStrategy then "simple"
    else if strategy eq normalScoringStrategy then "normal"
    else "normal"