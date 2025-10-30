package de.htwg.model

case class Table(height: Int, width: Int) {
  val cells: Array[Array[String]] = Array.ofDim[String](height, width)
  def set(height: Int, width: Int, card: String): Unit = cells(height)(width) = card

  //override def toString: String = super.toString TODO: for schleife, jede Zeile in Matrix darstellen und in String zusammenfassen
}