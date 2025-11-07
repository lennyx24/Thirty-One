package de.htwg.se.thirtyone.model

case class Card(symbol: Char, value: String, size: Int = 10) {
  val card: String = cardSize
  def bar: String = "+" + ("-" * size) + "+"
  def cells: String = "|" + (" " * size) + "|"
  def cardSize: String = {
    bar + "\n" +
    (cells + "\n") * (size/2) +
    bar + "\n"
  }
}
