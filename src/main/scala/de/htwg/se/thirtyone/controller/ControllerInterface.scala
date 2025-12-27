package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._

trait ControllerInterface {
    def handleInput(input: String): Unit

    def pass(): Unit
    def knock(): Unit
    def swap(): Unit

    def selectNumber(idx: String): Unit
    def selectAll(): Unit

    def undo(): Unit
    def redo(): Unit

    def getPlayersLength(): Int
    def getPlayerScore(player: Int): Double
    def getPlayersHealth(player: Int): Int
    def getPlayersHand(): List[Card]

    def getTableCard(): List[Card]
    def getTableString(): String
    def getTableGrid(): Vector[Vector[Option[Card]]]
}
