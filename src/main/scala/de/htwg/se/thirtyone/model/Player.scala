package de.htwg.se.thirtyone.model

case class Player(
    hasKnocked: Boolean = false, 
    points: Double = 0,
    playersHealth: Int = 3
)
