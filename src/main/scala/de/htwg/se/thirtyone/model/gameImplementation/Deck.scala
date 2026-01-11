package de.htwg.se.thirtyone.model.gameImplementation

case class Deck(size: Int = 10) {

  val symbols: List[Char] = '♦' :: '♠' :: '♥' :: '♣' :: Nil
  val numbers: List[String] = "2" :: "3" :: "4" :: "5" :: "6" :: "7" :: "8" :: "9" :: "10" :: "J" :: "Q" :: "K" :: "A" :: Nil
  val numbersSmall: List[String] = "7" :: "8" :: "9" :: "10" :: "J" :: "Q" :: "K" :: "A" :: Nil

  val deck: Vector[Card] = (for {
    s <- symbols
    n <- numbers
  } yield Card(s, n, size)).toVector

  val smallDeck: Vector[Card] = (for {
    s <- symbols
    n <- numbersSmall
  } yield Card(s, n, size)).toVector
}