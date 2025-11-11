package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model.*

import scala.io.StdIn.readLine
import scala.util.Random

object GameManager {
  private val cardPositions = List(
    List((1, 3), (1, 4), (1, 5)), //Position Middle Cards
    List((0, 1), (0, 2), (0, 3)), //Position Player 1
    List((0, 5), (0, 6), (0, 7)), //Position Player 2
    List((2, 1), (2, 2), (2, 3)), //Position Player 3
    List((2, 5), (2, 6), (2, 7)), //Position Player 4
  )

  private var gameRunning: Boolean = true

  def main(args: Array[String]): Unit = {
    print("Enter Player Amount: ")
    val playerCount: Int = readLine().toInt
    val cardDeck: Deck = Deck()

    val gameTable: Table =
      (0 to playerCount).foldLeft(Table()) { (t, i) =>
        val cards: List[Card] = List(
          cardDeck.deck(Random.nextInt(cardDeck.deck.length)),
          cardDeck.deck(Random.nextInt(cardDeck.deck.length)),
          cardDeck.deck(Random.nextInt(cardDeck.deck.length))
        )
        t.set(cardPositions(i), cards)
      }

    print(gameTable)

    var playersTurn: Int = 1
    while(gameRunning) {
      if (playersTurn > playerCount) playersTurn = 1

      var playerChosen: Boolean = false
      while (!playerChosen) {
        printf("Spieler %d ist dran, welchen Zug willst du machen? (Passen, Klopfen, Tauschen): ", playersTurn)
        val playersChoice: String = readLine()
        playersChoice match {
          case "Passen" =>
            print(gameTable)
            printf("Spieler %d passt diese Runde\n", playersTurn)
            playerChosen = true
          case "Klopfen" =>
            print(gameTable)
            printf("Spieler %d klopft diese Runde\n", playersTurn)
            //TODO:eine Runde noch, dann fertig
            playerChosen = true
          case "Tauschen" =>
            printf("Spieler %d will diese Runde eine Karte tauschen\n", playersTurn)
            //Karte tauschen
            printf("Spieler %d, welche Karte möchtest du abgeben? (1,2,3,alle)\n", playersTurn)
            val indexToGive = readLine()
            indexToGive match {
              case "1" =>
                printf("Spieler %d, welche Karte möchtest du erhalten?\n", playersTurn)
                val indexToReceive: Int = readLine().toInt

                //TODO:get card from table funktion fehlt

              //TODO:Karte 1 mit cardToReceive tauschen

                print(gameTable)
              case "2" =>
                printf("Spieler %d, welche Karte möchtest du erhalten?\n", playersTurn)
                val indexToReceive: Int = readLine().toInt
              //TODO:Karte 2 mit cardToReceive tauschen
                print(gameTable)
              case "3" =>
                printf("Spieler %d, welche Karte möchtest du erhalten?\n", playersTurn)
                val indexToReceive: Int = readLine().toInt
              //TODO:Karte 3 mit cardToReceive tauschen
                print(gameTable)
              case "alle" =>
              //TODO:alle Karten tauschen
                print(gameTable)
            }
            playerChosen = true
          case _ => printf("Spieler %d das ist keine valide Option\n", playersTurn)
        }
      }
      playersTurn += 1
    }
  }
}