package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model.*

class GameController(var gameState: GameState) extends Observable:

  def pass(playersTurn: Int): Unit = 
    gameState = gameState.pass(playersTurn)
    notifyObservers(PlayerPassed(playersTurn))

  def knock(playersTurn: Int): Unit = 
    gameState = gameState.knock(playersTurn)
    notifyObservers(PlayerKnocked(playersTurn))

  def swap(playersTurn: Int, indexGiveString: String, indexReceiveString: String): Unit =
    gameState = gameState.swap(gameState, playersTurn, indexGiveString, indexReceiveString)
    notifyObservers(PlayerSwapped(playersTurn))

  def gameFinished(playersTurn: Int): Unit =
    notifyObservers(GameEnded(playersTurn))
        
  def initializeGame(playerCount: Int): Unit = gameState = GameState(playerCount)