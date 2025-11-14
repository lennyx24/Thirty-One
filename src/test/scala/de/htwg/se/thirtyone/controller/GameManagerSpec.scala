package de.htwg.se.thirtyone.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

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
  }
}
