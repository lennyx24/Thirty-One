package de.htwg.se.thirtyone.util.fileio

import de.htwg.se.thirtyone.model.GameInterface

import java.nio.file._

trait FileIO {
  def save(game: GameInterface): Unit
  def load(): GameInterface
  def filepath = Paths.get(
    System.getProperty("user.dir"),
    "src", "main", "scala", "de", "htwg", "se", "thirtyone", "savedGame"
  )
}
