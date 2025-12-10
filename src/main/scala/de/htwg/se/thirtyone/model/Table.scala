package de.htwg.se.thirtyone.model

import de.htwg.se.thirtyone.model.factory.StandardGameFactory

import scala.util._

case class Table(grid: Vector[Vector[Option[Card]]] = Vector.fill(3, 9)(Option.empty[Card])):
  val height: Int = 3
  val width: Int = 9

  def get(pos: (Int, Int)): Card =
    grid(pos._1)(pos._2).get
    
  def getAll(player: Int): List[Card] =
    val cardPositions = StandardGameFactory.createGame(4).cardPositions
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
        i+=1
        g.updated(h, changedRow)
      }
    copy(grid = newGrid)

  def swap(pos1: (Int, Int), pos2: (Int, Int)): Table =
    val c1: Card = this.get((pos1._1, pos1._2))
    val c2: Card = this.get((pos2._1, pos2._2))
    val newTab1 = this.set((pos1._1, pos1._2), c2)
    val newTab2 = newTab1.set((pos2._1, pos2._2), c1)
    newTab2

  def indexes(cardDeck: Deck): Vector[Int] = Random.shuffle(cardDeck.deck.indices).toVector
  
  def createGameTable(playerCount: Int, indexes: Vector[Int], cardPositions: List[List[(Int, Int)]], cardDeck: Deck): Table =
    val (table, _) = (0 to playerCount).foldLeft(Table(), indexes) { case((t, idxs), i) =>
      val takeCount = cardPositions(i).length
      val (taken, rest) = idxs.splitAt(takeCount)
      val cards: List[Card] = taken.map(cardDeck.deck).toList
      (t.setAll(cardPositions(i), cards), rest)
    }
    table

  override def toString: String = {
    val invisibleCard: InvisibleCard= InvisibleCard()
    grid.foldLeft("") { (output, row) =>
      val (barString, topCellString, cellString, sizeCard) =
        row.foldLeft(("" ,"" ,"" , 0)) { case((bar, topCell, cell, size), idx)=>
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
      
      output + barNL + topNL + repeatCells + barNL
    }
  }