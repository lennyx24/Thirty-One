package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._

import scala.io.StdIn.readLine

case class GameManager(
  cardPositions: List[List[(Int, Int)]] = List(
    List((1, 3), (1, 4), (1, 5)), //Position Middle Cards
    List((0, 1), (0, 2), (0, 3)), //Position Player 1
    List((0, 5), (0, 6), (0, 7)), //Position Player 2
    List((2, 5), (2, 6), (2, 7)), //Position Player 3
    List((2, 1), (2, 2), (2, 3)), //Position Player 4
  ),
  gameTable: Table = Table(),
  gameRunning: Boolean = true
):

  def printNewRound(gameTable: Table): Unit = 
    (1 until 20).foreach(x => println)
    print(gameTable)
    (1 until 20).foreach(x => println)

  def pass(playersTurn: Int): String = "Spieler " + playersTurn + " passt diese Runde\n"

  def knock(playersTurn: Int): String = 
    //TODO:eine Runde noch, dann fertig
    "Spieler " + playersTurn + " klopft diese Runde\n"

  def calculateIndex(indexToGive: String): Int = indexToGive.toInt - 1

  def InvalidMove(playersTurn: Int): String = s"Spieler $playersTurn das ist keine valide Option\n"

  def swap(playersTurn: Int, gameT: Table, indexToGive: String, indexReceive: Int): GameManager =
    val stopRecursion = 3

    if indexReceive > 2 then this.copy(gameTable = gameT)
    else
      indexToGive match
        case "1" | "2" | "3" =>
          val indexGive = calculateIndex(indexToGive)
          val nextGameT = gameT.swap(cardPositions(playersTurn)(indexGive), cardPositions(0)(indexReceive))
          swap(playersTurn, nextGameT, indexToGive, stopRecursion)
        case "alle" =>
          val nextGameT = gameT.swap(cardPositions(playersTurn)(indexReceive), cardPositions(0)(indexReceive))
          swap(playersTurn, nextGameT, indexToGive, indexReceive + 1)

object GameManager:
  def main(args: Array[String]): Unit =
    print("Enter Player Amount: ")
    val playerCount: Int = readLine().toInt
    val cardDeck: Deck = Deck()

    val gm0 = GameManager()
    val gm = gm0.copy(gameTable = Table().createGameTable(playerCount, cardDeck, gm0.cardPositions))
    gameLoop(gm, 1)

    def gameLoop(gm: GameManager, playersTurn0: Int): Unit =
      if !gm.gameRunning then ()
      else
        val playersTurn =
          if playersTurn0 > playerCount then 1 else playersTurn0

        gm.printNewRound(gm.gameTable)
        print(s"Spieler $playersTurn ist dran, welchen Zug willst du machen? (Passen, Klopfen, Tauschen): ")
        val playersChoice = readLine()
        playersChoice match
          case "Passen" | "passen" =>
            print(gm.pass(playersTurn))
            gameLoop(gm, playersTurn + 1) 

          case "Klopfen" | "klopfen" =>
            print(gm.knock(playersTurn))
            gameLoop(gm, playersTurn + 1)

          case "Tauschen" | "tauschen" =>
            print(s"Spieler $playersTurn, welche Karte möchtest du abgeben? (1, 2, 3, alle): ")
            val indexToGive = readLine()
            indexToGive match
              case "1" | "2" | "3" =>
                printf(s"Spieler $playersTurn, welche Karte möchtest du erhalten? (1, 2, 3): ")
                val indexToReceive = readLine()
                val indexReceive = gm.calculateIndex(indexToReceive)
                if indexReceive > 2 || indexReceive < 0 then 
                  print(gm.InvalidMove(playersTurn))
                  gameLoop(gm, playersTurn)
                val nextGm = gm.swap(playersTurn, gm.gameTable, indexToGive, indexReceive)
                gameLoop(nextGm, playersTurn + 1)

              case "alle" =>
                val nextGm = gm.swap(playersTurn, gm.gameTable, indexToGive, 0)
                gameLoop(nextGm, playersTurn + 1)

              case _ => 
                print(gm.InvalidMove(playersTurn))
                gameLoop(gm, playersTurn)

          case _ => 
            
            gameLoop(gm, playersTurn)
