package de.htwg.se.thirtyone.model.factory

import de.htwg.se.thirtyone.model.GameData

trait GameFactory:
  def createGame(playerAmount: Int): GameData
