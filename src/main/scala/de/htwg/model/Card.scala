package de.htwg.model

case class Card() {
  def bar(size: Int): String = "+" + ("-" * size) + "+"
  def cells(size: Int): String = "|" + (" " * size) + "|"
}
