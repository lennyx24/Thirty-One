package de.htwg.se.thirtyone.model

case class Table(
  height: Int = 3,
  width: Int = 9,
  grid: Vector[Vector[Option[Card]]] = Vector.fill(3, 9)(Option.empty[Card])
) {
  def set(h: Int, w: Int, card: Card): Table = {
    val row = grid(h)
    val newRow = row.updated(w, Some(card))
    val newTbl = grid.updated(h, newRow)
    copy(grid = newTbl)
  }

  override def toString: String = {
    val invisibleCard: InvisibleCard = InvisibleCard()
    val output: StringBuilder = new StringBuilder()
    grid.foreach { row =>
      val barString: StringBuilder = new StringBuilder()
      val topCellString: StringBuilder = new StringBuilder()
      val cellString: StringBuilder = new StringBuilder()

      var sizeCard: Int = 0

      for (i <- row.indices) {
        row(i) match {
          case Some(s) => {
            barString ++= s.bar
            topCellString ++= s.topCell
            cellString ++= s.cells

            sizeCard = s.size
          }
          case None => {
            barString ++= invisibleCard.invCell
            topCellString ++= invisibleCard.invCell
            cellString ++= invisibleCard.invCell
          }
        }
      }
      barString ++= "\n"
      topCellString ++= "\n"
      cellString ++= "\n"

      output ++= barString
      output ++= topCellString
      for (i <- 1 until (sizeCard/2)) output ++= cellString
      output ++= barString
    }
    output.result()
  }
}