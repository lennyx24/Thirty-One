package de.htwg.se.thirtyone.fileio

import de.htwg.se.thirtyone.model.GameInterface

trait FileIO {
  val name = "src\\main\\scala\\de\\htwg\\se\\thirtyone\\savedGame"
  def save(game: GameInterface): Unit
  def load(): GameInterface
}
