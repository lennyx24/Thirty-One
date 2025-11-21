package de.htwg.se.thirtyone.model

trait GameEvent

case object GameStarted extends GameEvent

case class PlayerPassed(player: Int) extends GameEvent

case class PlayerKnocked(player: Int) extends GameEvent

case class PlayerSwapped(player: Int) extends GameEvent

case class GameEnded(winner: Int) extends GameEvent