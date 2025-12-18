package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model.GameData
import de.htwg.se.thirtyone.util.{Observer, GameEvent}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.thirtyone.controller.state._

class GameControllerSpec extends AnyWordSpec with Matchers {

  "A GameController" should {
    val gameData = GameData(2)

    var inputReceived = ""
    val mockState = new ControllerState {
      override def execute(input: String, controller: GameController): Unit = {
        inputReceived = input
      }
    }

    val controller = new GameController(mockState, gameData)

    "delegate handleInput to the current state logic (execute)" in {
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

    "support undo and redo" in {
      val gd = GameData(2)
      val c = new GameController(PlayingState, gd)
      
      // Initial state
      val initialPlayer = c.gameData.currentPlayerIndex
      
      // Execute pass
      c.pass()
      c.gameData.currentPlayerIndex should not be initialPlayer
      
      // Undo
      c.undo()
      c.gameData.currentPlayerIndex should be(initialPlayer)
      
      // Redo
      c.redo()
      c.gameData.currentPlayerIndex should not be initialPlayer
    }
  }
}