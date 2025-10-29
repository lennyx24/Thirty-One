package de.htwg.model

case class Card(size: Int) {
  val card: String = cardSize(size)
  def bar(size: Int): String = "+" + ("-" * size) + "+"
  def invBar(size: Int): String = " " * size + "  "
  def cells(size: Int): String = "|" + (" " * size) + "|"
  def invCells(size: Int): String = " " * size + "  "
  def cardSize(size: Int): String = {
    bar(size) + "\n" +
    (cells(size) + "\n") * (size/2) + 
    bar(size) + "\n"
  }
  def invCard(size: Int): String = {
    invBar(size) + "\n" +
      (invCells(size) + "\n") * (size / 2) +
      invBar(size) + "\n"
  }
}
