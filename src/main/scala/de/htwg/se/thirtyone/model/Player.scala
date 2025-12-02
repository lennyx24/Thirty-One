package de.htwg.se.thirtyone.model

case class Player(
    hasKnocked: Boolean = false, 
    points: Double = 0,
    playersHealth: Int = 2,
    isAlive: Boolean = true
):
    def receiveDamage(amount: Int): Player = if playersHealth > 1 then copy(playersHealth = playersHealth-1) else copy(isAlive = false)
