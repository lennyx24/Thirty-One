package de.htwg.se.thirtyone.model.game

case class Deck(size: Int = 10):
  private val symbols: List[Char] = List('♦', '♠', '♥', '♣')
  private val ranks: List[String] = List("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")
  private val smallRanks: List[String] = List("7", "8", "9", "10", "J", "Q", "K", "A")

  val deck: Vector[Card] = (for
    s <- symbols
    n <- ranks
  yield Card(s, n, size)).toVector

  val smallDeck: Vector[Card] = (for
    s <- symbols
    n <- smallRanks
  yield Card(s, n, size)).toVector
