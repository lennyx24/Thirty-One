package de.htwg.se.thirtyone.model

case class Table(height: Int = 3, width: Int = 9) {
  val table: Vector[Vector[Option[Card]]] = Vector.fill(height, width)(None)
  
  def set(height: Int, width: Int, card: Card): Vector[Vector[Option[Card]]] = {
    val row = table(height)
    val newRow = row.updated(width, Some(card))
    val newTbl = table.updated(height, newRow)
    newTbl
  }

  override def toString: String = {
    val invisibleCard: InvisibleCard = InvisibleCard()
    val output: StringBuilder = new StringBuilder()
    table.foreach { row =>
      row.foreach {
        case Some(s) => output ++= s.cardSize
        case None => output ++= invisibleCard.invCard
      }
      output ++= "\n"
    }
    output.result()
  }
}