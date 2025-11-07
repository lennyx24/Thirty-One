package de.htwg.model

object Deck {
  def size(s: Int): Vector[Card] = {
    Vector(
      //diamonds
      Card('d', "2", s),
      Card('d', "3", s),
      Card('d', "4", s),
      Card('d', "5", s),
      Card('d', "6", s),
      Card('d', "7", s),
      Card('d', "8", s),
      Card('d', "9", s),
      Card('d', "10", s),
      Card('d', "J", s),
      Card('d', "Q", s),
      Card('d', "K", s),
      Card('d', "A", s),
      //spades
      Card('s', "2", s),
      Card('s', "3", s),
      Card('s', "4", s),
      Card('s', "5", s),
      Card('s', "6", s),
      Card('s', "7", s),
      Card('s', "8", s),
      Card('s', "9", s),
      Card('s', "10", s),
      Card('s', "J", s),
      Card('s', "Q", s),
      Card('s', "K", s),
      Card('s', "A", s),
      //hearts
      Card('h', "2", s),
      Card('h', "3", s),
      Card('h', "4", s),
      Card('h', "5", s),
      Card('h', "6", s),
      Card('h', "7", s),
      Card('h', "8", s),
      Card('h', "9", s),
      Card('h', "10", s),
      Card('h', "J", s),
      Card('h', "Q", s),
      Card('h', "K", s),
      Card('h', "A", s),
      //clubs
      Card('c', "2", s),
      Card('c', "3", s),
      Card('c', "4", s),
      Card('c', "5", s),
      Card('c', "6", s),
      Card('c', "7", s),
      Card('c', "8", s),
      Card('c', "9", s),
      Card('c', "10", s),
      Card('c', "J", s),
      Card('c', "Q", s),
      Card('c', "K", s),
      Card('c', "A", s)
    )
  }
}