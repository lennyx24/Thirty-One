package de.htwg.se.thirtyone.model.gameImplementation

case class Deck(size: Int = 10) {

  private val symbols: List[Char] = List('♦', '♠', '♥', '♣')
  private val numbers: List[String] = List("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")
  private val numbersSmall: List[String] = List("7", "8", "9", "10", "J", "Q", "K", "A")

  val deck: Vector[Card] = (for {
    s <- symbols
    n <- numbers
  } yield Card(s, n, size)).toVector

  val smallDeck: Vector[Card] = (for {
    s <- symbols
    n <- numbersSmall
  } yield Card(s, n, size)).toVector
}