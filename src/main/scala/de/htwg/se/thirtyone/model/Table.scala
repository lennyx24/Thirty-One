package de.htwg.se.thirtyone.model

case class Table(grid: Vector[Vector[Option[Card]]] = Vector.fill(3, 9)(Option.empty[Card])) {
  val height: Int = 3
  val width: Int = 9

  def get(pos: (Int, Int)): Card = {
    val oc = grid(pos._1)(pos._2)
    oc match {
      case Some(c) => c
      case None => throw new NoSuchElementException("No card at position " + pos)
    }
  }

  def set(pos: (Int, Int), card: Card): Table = {
    val changedRow = grid(pos._1).updated(pos._2, Some(card))
    val newGrid = grid.updated(pos._1, changedRow)
    copy(grid = newGrid)
  }

  def setAll(pos: List[(Int, Int)], cards: List[Card]): Table = {
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

  def swap(pos1: (Int, Int), pos2: (Int, Int)): Table = {
    val c1: Card = this.get((pos1._1, pos1._2))
    val c2: Card = this.get((pos2._1, pos2._2))
    val newTab1 = this.set((pos1._1, pos1._2), c2)
    val newTab2 = newTab1.set((pos2._1, pos2._2), c1)
    newTab2
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