package de.htwg.se.thirtyone.model

object GameScoringStrategy:
    var strategy = normalScoringStrategy

    private def cardPoints(card: Card): Double =
        card.value match
            case "A" => 11
            case "K" | "Q" | "J" => 10
            case _ => card.value.toDouble
        
    def simpleScoringStrategy(cards: List[Card]): Double =
        cards.map(cardPoints).sum

    def normalScoringStrategy(cards: List[Card]): Double =
        val bySymbol = cards.groupBy(_.symbol)
        val pointsPerSymbol = bySymbol.map {
            case (_, symbolCards) => symbolCards.map(cardPoints).sum
        }

        val byValue = cards.groupBy(_.value)
        val threeOfAKind = if byValue.size == 1 then 30.5 else 0.0

        Math.max(pointsPerSymbol.max, threeOfAKind)
