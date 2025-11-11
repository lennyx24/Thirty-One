package de.htwg.se.thirtyone.model

case class Table(grid: Vector[Vector[Option[Card]]] = Vector.fill(3, 9)(Option.empty[Card])) {
  val height: Int = 3
  val width: Int = 9

  def get(h: Int, w: Int): Card = {
    grid.lift(h).flatMap(_.lift(w)).flatten.getOrElse(
      throw new NoSuchElementException(s"Keine Karte an Position ($h,$w)")
    )
  }

  def set(h: Int, w: Int, card: Card): Table = {
    val changedRow = grid(h).updated(w, Some(card))
    val newGrid = grid.updated(h, changedRow)
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

  def swap(h1: Int, w1: Int, h2: Int, w2: Int): Table = {
    val c1: Card = this.get(h1, w1)
    val c2: Card = this.get(h2, w2)
    val newTab1 = this.set(h1, w1, c2)
    val newTab2 = newTab1.set(h2, w2, c1)
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