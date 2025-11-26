package de.htwg.se.thirtyone.model

case class Player(
    hasKnocked: Boolean = false, 
    points: Int = 0,
    playersNumber: Int,
    playersHealth: Int = 3
)
