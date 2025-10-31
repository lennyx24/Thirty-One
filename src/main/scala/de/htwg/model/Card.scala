package de.htwg.model

case class Card(symbol: Char, value: String, size: Int = 10) {
  val card: String = cardSize
  def bar: String = "+" + ("-" * size) + "+"
  def cells: String = "|" + (" " * size) + "|"
  def cardSize: String = {
    bar + "\n" +
    (cells + "\n") * (size/2) +
    bar + "\n"
  }

  //def invBar(size: Int): String = " " * size + "  "
  //def invCells(size: Int): String = " " * size + "  "
  //def invCard(size: Int): String = {
  //  invBar(size) + "\n" +
  //    (invCells(size) + "\n") * (size / 2) +
  //    invBar(size) + "\n"
  //}
}
