package de.htwg.model

case class Table(height: Int = 3, width: Int = 9) {
  val table: Vector[Vector[Option[Card]]] = Vector.fill(height, width)(None)
  
  def set(height: Int, width: Int, card: Card): Vector[Vector[Option[Card]]] = {
    val row = table(height)
    val newRow = row.updated(width, Some(card))
    val newTbl = table.updated(height, newRow)
    newTbl
  }
  
  //override def toString: String = super.toString TODO: for schleife, jede Zeile in Matrix darstellen und in String zusammenfassen
}