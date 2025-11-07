package de.htwg.model

case class Card(symbol: Char, value: String, size: Int = 10) {
  require(size > 3)
  private val valueString: String = symbol.toString + value
  val cardString: String = cardSize
  def bar: String = "+" + ("-" * size) + "+"
  def topCell: String = "| " + valueString +(" " * (size - valueString.length - 1)) + "|"
  def cells: String = "|" + (" " * (size)) + "|"
  def cardSize: String = {
    bar + "\n" +
    topCell + "\n" +
    (cells + "\n") * (size/2 - 1) +
    bar + "\n"
  }
}
