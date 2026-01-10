package de.htwg.se.thirtyone.model.factory

import de.htwg.se.thirtyone.model.gameImplementation.GameData

trait GameFactory:
  def createGame(playerAmount: Int): GameData
