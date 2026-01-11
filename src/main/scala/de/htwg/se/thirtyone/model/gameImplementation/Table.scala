package de.htwg.se.thirtyone.model.gameImplementation

import scala.util._

case class Table(grid: Vector[Vector[Option[Card]]] = Vector.fill(3, 9)(Option.empty[Card])):
  val height: Int = 3
  val width: Int = 9

  def get(pos: (Int, Int)): Card =
    grid(pos._1)(pos._2).get

  def getAll(player: Int, cardPositions: List[List[(Int, Int)]]): List[Card] =
    List(
      this.get(cardPositions(player)(0)),
      this.get(cardPositions(player)(1)),
      this.get(cardPositions(player)(2))
    )

  def set(pos: (Int, Int), card: Card): Table =
    val changedRow = grid(pos._1).updated(pos._2, Some(card))
    val newGrid = grid.updated(pos._1, changedRow)
    copy(grid = newGrid)

  def setAll(pos: List[(Int, Int)], cards: List[Card]): Table =
    var i: Int = 0
    val newGrid =
      pos.foldLeft(grid) { case (g, (h, w)) =>
        val row = g(h)
        val changedRow = row.updated(w, Some(cards(i)))
        i += 1
        g.updated(h, changedRow)
      }
    copy(grid = newGrid)

  def swap(pos1: (Int, Int), pos2: (Int, Int)): Table =
    val c1: Card = this.get((pos1._1, pos1._2))
    val c2: Card = this.get((pos2._1, pos2._2))
    val newTab1 = this.set((pos1._1, pos1._2), c2)
    val newTab2 = newTab1.set((pos2._1, pos2._2), c1)
    newTab2

  def indexes(cardDeck: Vector[Card]): Vector[Int] = Random.shuffle(cardDeck.indices).toVector

  def createGameTable(playerCount: Int, indexes: Vector[Int], cardPositions: List[List[(Int, Int)]], cardDeck: Vector[Card]): (Table, Int) =
    val (table, remaining) = (0 to playerCount).foldLeft(Table(), indexes) { case ((t, idxs), i) =>
      val takeCount = cardPositions(i).length
      val (taken, rest) = idxs.splitAt(takeCount)
      val cards: List[Card] = taken.map(cardDeck).toList
      (t.setAll(cardPositions(i), cards), rest)
    }
    (table, indexes.length - remaining.length)

  def newMiddleCards(indexes: Vector[Int], cardPositions: List[(Int, Int)], cardDeck: Vector[Card], drawIndex: Int): (Table, Int) =
    val available = indexes.drop(drawIndex)
    if (available.length < 3) return (this, drawIndex)
    val newTab = (0 until 3).foldLeft(this) { (tab, i) =>
      tab.set(cardPositions(i), cardDeck(available(i)))
    }
    (newTab, drawIndex + 3)

  def printTable(players: List[Player]): String = {
    val invisibleCard: InvisibleCard = InvisibleCard()
    grid.zipWithIndex.foldLeft("") { case (output, (row, rowIndex)) =>
      val playerHeader = rowIndex match {
        case 0 =>
          val p1 = if (players.nonEmpty) s"Spieler 1: ${players(0).playersHealth} Leben, ${players(0).points} Punkte" else ""
          val p2 = if (players.length > 1) s"Spieler 2: ${players(1).playersHealth} Leben, ${players(1).points} Punkte" else ""
          if (p1.nonEmpty || p2.nonEmpty) " " * 13 + f"$p1%-52s" + p2 + "\n" else ""

        case 2 =>
          val p4 = if (players.length > 3) s"Spieler 4: ${players(3).playersHealth} Leben, ${players(3).points} Punkte" else ""
          val p3 = if (players.length > 2) s"Spieler 3: ${players(2).playersHealth} Leben, ${players(2).points} Punkte" else ""
          if (p4.nonEmpty || p3.nonEmpty) " " * 13 + f"$p4%-52s" + p3 + "\n" else ""

        case _ => ""
      }

      val (barString, topCellString, cellString, sizeCard) =
        row.foldLeft(("", "", "", 0)) { case ((bar, topCell, cell, size), idx) =>
          idx match
            case Some(card) =>
              (bar + card.bar, topCell + card.topCell, cell + card.cells, card.size)
            case None =>
              (bar + invisibleCard.invCell, topCell + invisibleCard.invCell, cell + invisibleCard.invCell, size)
        }
      val barNL = barString + "\n"
      val topNL = topCellString + "\n"
      val cellNL = cellString + "\n"

      val repeatCount = math.max(0, sizeCard / 2 - 1)
      val repeatCells = List.fill(repeatCount)(cellNL).mkString

      output + playerHeader + barNL + topNL + repeatCells + barNL
    }
  }