package de.htwg.se.thirtyone.fileio.implementation

import de.htwg.se.thirtyone.fileio.FileIO
import de.htwg.se.thirtyone.model.GameInterface
import de.htwg.se.thirtyone.model.gameImplementation.GameData
import scala.xml._

class XmlFileIO extends FileIO{
  val file = name + ".xml"
  override def save(game: GameInterface): Unit =
    val xml = game.toXml()
    XML.save(file, xml)

  override def load(): GameInterface =
    val xml = XML.loadFile(file)
    GameData.loadGame(xml)
}
