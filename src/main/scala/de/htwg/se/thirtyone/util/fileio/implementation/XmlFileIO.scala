package de.htwg.se.thirtyone.util.fileio.implementation

import de.htwg.se.thirtyone.model.GameInterface
import de.htwg.se.thirtyone.model.game.GameData
import de.htwg.se.thirtyone.util.fileio.FileIO
import scala.xml.XML

class XmlFileIO extends FileIO:
  private val file = filepath.toString + ".xml"
  override def save(game: GameInterface): Unit =
    val xml = game.toXml()
    XML.save(file, xml)

  override def load(): GameInterface =
    val xml = XML.loadFile(file)
    GameData.loadGame(xml)
