package de.htwg.model

case class Table(height: Int, width: Int) {
  val cells: Array[Array[Card]] = Array.ofDim[Card](height, width)
  def set(height: Int, width: Int, card: Card): Unit = cells(height)(width) = card
}