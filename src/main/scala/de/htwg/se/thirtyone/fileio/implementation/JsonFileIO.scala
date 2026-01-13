package de.htwg.se.thirtyone.fileio.implementation

import de.htwg.se.thirtyone.fileio.FileIO
import de.htwg.se.thirtyone.model.GameInterface

class JsonFileIO extends FileIO{
  override def save(game: GameInterface): Unit = ???

  override def load(): GameInterface = ???
}
