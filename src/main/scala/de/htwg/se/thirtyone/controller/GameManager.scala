package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._

import scala.io.StdIn.readLine
import scala.util.Random

object GameManager { //TODO: case class mit gameRunning und gameTable als Parameter (alle vars im Code!)
  private val cardPositions = List(
    List((1, 3), (1, 4), (1, 5)), //Position Middle Cards
    List((0, 1), (0, 2), (0, 3)), //Position Player 1
    List((0, 5), (0, 6), (0, 7)), //Position Player 2
    List((2, 5), (2, 6), (2, 7)), //Position Player 3
    List((2, 1), (2, 2), (2, 3)), //Position Player 4
  )

  private var gameRunning: Boolean = true

  private def printNewRound(gameTable: Table): Unit = {
    for (i <- 1 until 50) {
      println()
    }
    print(gameTable)
  }

  def pass(playersTurn: Int): String = {
    "Spieler " + playersTurn + " passt diese Runde\n"
  }

  def knock(playersTurn: Int): String = {
    //TODO:eine Runde noch, dann fertig
    "Spieler " + playersTurn + " klopft diese Runde\n"
  }

  def swapAll(playersTurn: Int, gameT: Table): Table = {
    var gT: Table = gameT
    gT = gameT.swap(cardPositions(playersTurn)(0), cardPositions(0)(0))
    gT = gT.swap(cardPositions(playersTurn)(1), cardPositions(0)(1))
    gT = gT.swap(cardPositions(playersTurn)(2), cardPositions(0)(2))
    gT
  }

  def calculateIndex(indexToGive: String): Int = indexToGive.toInt - 1

  def swap(playersTurn: Int, gameT: Table): Table = {
    var gT: Table = gameT
    var swapped: Boolean = false
    while (!swapped) {
      printf("Spieler %d, welche Karte möchtest du abgeben? (1,2,3,alle): ", playersTurn)
      val indexToGive = readLine()
      indexToGive match {
        case "1" | "2" | "3" =>
          val indexGive = calculateIndex(indexToGive)
          printf("Spieler %d, welche Karte möchtest du erhalten? (1,2,3): ", playersTurn)
          val indexToReceive = readLine()
          val indexReceive = calculateIndex(indexToReceive)
          if (indexReceive > 2 || indexReceive < 0) {
            printf("Spieler %d das ist keine valide Option\n", playersTurn)
          } else {
            gT = gameT.swap(cardPositions(playersTurn)(indexGive), cardPositions(0)(indexReceive))
            swapped = true
          }
        case "alle" =>
          gT = swapAll(playersTurn, gameT)
          swapped = true
        case _ =>
          printf("Spieler %d das ist keine valide Option\n", playersTurn)
      }
    }
    gT
  }

  private def createGameTable(playerCount: Int, cardDeck: Deck): Table = {
    (0 to playerCount).foldLeft(Table()) { (t, i) =>
      val cards: List[Card] = List(
        cardDeck.deck(Random.nextInt(cardDeck.deck.length)),
        cardDeck.deck(Random.nextInt(cardDeck.deck.length)),
        cardDeck.deck(Random.nextInt(cardDeck.deck.length))
      )
      t.setAll(cardPositions(i), cards)
    }
  }

  def main(args: Array[String]): Unit = {
    print("Enter Player Amount: ")
    val playerCount: Int = readLine().toInt
    val cardDeck: Deck = Deck()

    var gameTable: Table = createGameTable(playerCount, cardDeck)
    print(gameTable)

    var playersTurn: Int = 1
    while (gameRunning) {
      if (playersTurn > playerCount) playersTurn = 1

      var playerChosen: Boolean = false
      while (!playerChosen) {
        printf("Spieler %d ist dran, welchen Zug willst du machen? (Passen, Klopfen, Tauschen): ", playersTurn)
        val playersChoice: String = readLine()
        playersChoice match {
          case "Passen" | "passen" =>
            print(pass(playersTurn))
            playerChosen = true

          case "Klopfen" | "klopfen" =>
            print(knock(playersTurn))
            playerChosen = true

          case "Tauschen" | "tauschen" =>
            gameTable = swap(playersTurn, gameTable)
            playerChosen = true

          case _ => printf("Spieler %d das ist keine valide Option\n", playersTurn)
        }
        printNewRound(gameTable)
      }
      playersTurn += 1
    }
  }
}
