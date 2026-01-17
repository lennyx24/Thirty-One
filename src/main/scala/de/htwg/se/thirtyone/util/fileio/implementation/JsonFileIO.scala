package de.htwg.se.thirtyone.util.fileio.implementation

import de.htwg.se.thirtyone.util.fileio.FileIO
import de.htwg.se.thirtyone.model.GameInterface
import de.htwg.se.thirtyone.model.gameImplementation.GameData
import play.api.libs.json.*
import java.nio.charset.StandardCharsets
import java.nio.file.*

class JsonFileIO extends FileIO{
  val file = filepath.toString + ".json"
  override def save(game: GameInterface): Unit =
    val json = game.toJson()
    val str = Json.prettyPrint(json)
    Files.write(Paths.get(file), str.getBytes(StandardCharsets.UTF_8))

  override def load(): GameInterface =
    val path = Paths.get(file)
    if !Files.exists(path) then throw new Exception(s"Datei nicht gefunden: $file")
    val str = new String(Files.readAllBytes(path), StandardCharsets.UTF_8)
    val js = Json.parse(str)
    GameData.loadGame(js)
}
