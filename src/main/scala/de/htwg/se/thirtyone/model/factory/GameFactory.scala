package de.htwg.se.thirtyone.model.factory

import de.htwg.se.thirtyone.model.game.GameData

trait GameFactory:
  def createGame(playerCount: Int): GameData
