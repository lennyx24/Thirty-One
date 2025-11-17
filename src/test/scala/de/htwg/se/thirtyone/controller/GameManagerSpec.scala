package de.htwg.se.thirtyone.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

import de.htwg.se.thirtyone.model._

class GameManagerSpec extends AnyWordSpec {
  "GameManager" should{
    "be able to pass" in {
      val playersTurn = 1
      GameManager.pass(playersTurn) should be ("Spieler 1 passt diese Runde\n")
      val playersTurn2 = 3
      GameManager.pass(playersTurn2) should be("Spieler 3 passt diese Runde\n")
    }
    "be able to knock" in{
      val playersTurn = 1
      GameManager.knock(playersTurn) should be("Spieler 1 klopft diese Runde\n")
      val playersTurn2 = 3
      GameManager.knock(playersTurn2) should be("Spieler 3 klopft diese Runde\n")
    }
    "be able to swapAll" in {
      val playerTurn = 1
      var tab: Table = Table()
      tab = tab.set((0, 1), Card('♦', "10", 10))
      tab = tab.set((0, 2), Card('♦', "10", 10))
      tab = tab.set((0, 3), Card('♦', "10", 10))
      tab = tab.set((1, 3), Card('♦', "9", 10))
      tab = tab.set((1, 4), Card('♦', "9", 10))
      tab = tab.set((1, 5), Card('♦', "9", 10))
      tab = GameManager.swapAll(playerTurn, tab)
      tab.get((0,1)) should be (Card('♦', "9", 10))
      tab.get((1,3)) should be (Card('♦', "10", 10))
    }
    "have a calculatedIndex" in {
      GameManager.calculateIndex("3") should be(2)
      GameManager.calculateIndex("2") should be(1)
      GameManager.calculateIndex("4") should be(3)
    }
  }
}
