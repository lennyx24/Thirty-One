package de.htwg.se.thirtyone.model

trait GameFactory:
    def createGame(playerAmount: Int): GameData
