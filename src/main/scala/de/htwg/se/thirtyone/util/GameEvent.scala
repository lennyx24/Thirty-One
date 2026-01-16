package de.htwg.se.thirtyone.util

import de.htwg.se.thirtyone.model.gameImplementation.Player

trait GameEvent

case object GameStarted extends GameEvent

case object InvalidInput extends GameEvent

case object PrintTable extends GameEvent

case class RunningGame(player: Player) extends GameEvent

case class PlayerScore(player: Player) extends GameEvent

case class PlayerSwapGive(player: Player) extends GameEvent

case class PlayerSwapTake(player: Player) extends GameEvent

case class PlayerPassed(player: Player) extends GameEvent

case class PlayerKnocked(player: Player) extends GameEvent

case class PlayerSwapped(player: Player) extends GameEvent

case class RoundEnded(winner: Player) extends GameEvent

case class GameEnded(winner: Player) extends GameEvent

case class PlayerName(player: Int) extends GameEvent

case class PlayerNameSet(index: Int, name: String) extends GameEvent
