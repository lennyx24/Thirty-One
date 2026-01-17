package de.htwg.se.thirtyone

import de.htwg.se.thirtyone.util.fileio.FileIO
import de.htwg.se.thirtyone.model.GameInterface
import de.htwg.se.thirtyone.model.gameImplementation.GameData

object StubFileIO extends FileIO {
  override def save(game: GameInterface): Unit = ()
  override def load(): GameInterface = GameData(2)
}
