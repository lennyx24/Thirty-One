package de.htwg.model

case class Card() {
  def bar(size: Int): String = "+" + ("-" * size) + "+"
  def cells(size: Int): String = "|" + (" " * size) + "|"
  def cardSize(size: Int): String = {
    bar(size) + "\n" +
    (cells(size) + "\n") * (size/2 + 1) + 
    bar(size) + "\n"
  }
}
