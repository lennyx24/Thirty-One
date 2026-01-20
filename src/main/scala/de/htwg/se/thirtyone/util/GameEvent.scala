package de.htwg.se.thirtyone.util

trait GameEvent

case class PlayerInfo(id: String, name: String)

case object GameStarted extends GameEvent

case object InvalidInput extends GameEvent

case object PrintTable extends GameEvent

case class RunningGame(player: PlayerInfo) extends GameEvent

case class PlayerScore(player: PlayerInfo) extends GameEvent

case class PlayerSwapGive(player: PlayerInfo) extends GameEvent

case class PlayerSwapTake(player: PlayerInfo) extends GameEvent

case class PlayerPassed(player: PlayerInfo) extends GameEvent

case class PlayerKnocked(player: PlayerInfo) extends GameEvent

case class PlayerSwapped(player: PlayerInfo) extends GameEvent

case class RoundEnded(winner: PlayerInfo) extends GameEvent

case class GameEnded(winner: PlayerInfo) extends GameEvent

case class PlayerName(player: Int) extends GameEvent

case class PlayerNameSet(index: Int, name: String) extends GameEvent
