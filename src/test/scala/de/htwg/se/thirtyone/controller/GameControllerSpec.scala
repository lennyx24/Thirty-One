package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model.GameData
import de.htwg.se.thirtyone.util.{Observer, GameEvent} // Import für GameEvent und Observer
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameControllerSpec extends AnyWordSpec with Matchers {

  "A GameController" should {
    val gameData = GameData(2)

    var inputReceived = ""
    val cs = new ControllerState {
      override def execute(input: String, controller: GameController): Unit = {
        inputReceived = input
      }
    }

    val controller = new GameController(cs, gameData)

    "handle input" in {
      val testInput = "knock"
      controller.handleInput(testInput)

      inputReceived should be(testInput)
    }

    "allow changing the state" in {
      val newState = new ControllerState {
        override def execute(input: String, c: GameController): Unit = {}
      }
      
      controller.state = newState
      controller.state should be(newState)
    }
    "be able to gameFinished" in {
      val playersTurn = 1
      val beforeIndex = gameController.gameState.currentPlayerIndex
      gameController.gameFinished(playersTurn)
      gameController.gameState.currentPlayerIndex should be(beforeIndex)
    }

    "allow changing the game data" in {
      val newGameData = GameData(4)
      controller.gameData = newGameData
      
      controller.gameData.playerCount should be(4)
    }
    
    "be an Observable" in {
      val observer = new Observer {
        override def update(e: GameEvent): Unit = {} 
      }
      controller.add(observer)
      controller.subscribers should contain(observer)
    }
  }
}