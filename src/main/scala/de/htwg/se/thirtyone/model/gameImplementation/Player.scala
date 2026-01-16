package de.htwg.se.thirtyone.model.gameImplementation

import java.util.UUID

case class Player(
    name: String = "Nameless Player",
    hasKnocked: Boolean = false, 
    points: Double = 0,
    playersHealth: Int = 3,
    isAlive: Boolean = true,
    hasPassed:Boolean = false,
    id: String = UUID.randomUUID().toString
):
    def receiveDamage(amount: Int): Player = if playersHealth > 1 then copy(playersHealth = playersHealth-1) else copy(isAlive = false)
    def changeName(newName: String): Player = copy(name = newName)
