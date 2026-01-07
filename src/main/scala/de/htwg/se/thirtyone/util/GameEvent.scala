package de.htwg.se.thirtyone.util

import de.htwg.se.thirtyone.model.Player

trait GameEvent

case object GameStarted extends GameEvent

case object InvalidInput extends GameEvent

case object PrintTable extends GameEvent

case class RunningGame(player: Player) extends GameEvent

case class PlayerScore(player: Int) extends GameEvent

case class PlayerSwapGive(player: Int) extends GameEvent

case class PlayerSwapTake(player: Int) extends GameEvent

case class PlayerPassed(player: Int) extends GameEvent

case class PlayerKnocked(player: Int) extends GameEvent

case class PlayerSwapped(player: Int) extends GameEvent

case class GameEnded(winner: Int) extends GameEvent