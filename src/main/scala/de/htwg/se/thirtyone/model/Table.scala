package de.htwg.se.thirtyone.model

case class Table(grid: Vector[Vector[Option[Card]]] = Vector.fill(3, 9)(Option.empty[Card])) {
  val height: Int = 3
  val width: Int = 9

  def set(pos: List[(Int, Int)], cards: List[Card]): Table = {
    var i: Int = 0
    val newGrid =
      pos.foldLeft(grid) { case (g, (h, w)) =>
        val row = g(h)
        val changedRow = row.updated(w, Some(cards(i)))
        i+=1
        g.updated(h, changedRow)
      }
    copy(grid = newGrid)
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