package de.htwg.se.thirtyone.fileio.implementation

import de.htwg.se.thirtyone.model.gameImplementation.{GameData, Player, Table}
import de.htwg.se.thirtyone.model.factory.StandardGameFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import java.nio.file.{Files, Paths, Path}
import de.htwg.se.thirtyone.model.GameInterface
import de.htwg.se.thirtyone.util.fileio.implementation.{JsonFileIO, XmlFileIO}

class FileIOSpec extends AnyWordSpec with Matchers {

  class TestJsonFileIO extends JsonFileIO {
    override def filepath: Path = Paths.get("target", "test_save_game")
  }

  class TestXmlFileIO extends XmlFileIO {
    override def filepath: Path = Paths.get("target", "test_save_game")
  }

  def cleanUp(): Unit = {
    Files.deleteIfExists(Paths.get("target", "test_save_game.json"))
    Files.deleteIfExists(Paths.get("target", "test_save_game.xml"))
  }

  "JsonFileIO" should {
    "save and load the game correctly" in {
      cleanUp()
      val game = StandardGameFactory.createGame(2)
      val modifiedGame = game.changePlayerName(game.players.head, "TesterJson")
      
      val fileIO = new TestJsonFileIO
      fileIO.save(modifiedGame)
      
      val loadedGame = fileIO.load()
      
      loadedGame.playerCount shouldBe modifiedGame.playerCount
      loadedGame.players.head.name shouldBe "TesterJson"
      loadedGame.currentPlayerIndex shouldBe modifiedGame.currentPlayerIndex
      loadedGame.table.grid.length shouldBe 3
      
      cleanUp()
    }

    "throw exception if file not found" in {
      cleanUp()
      val fileIO = new TestJsonFileIO
      an [Exception] should be thrownBy fileIO.load()
    }
  }

  "XmlFileIO" should {
    "save and load the game correctly" in {
      cleanUp()
      val game = StandardGameFactory.createGame(2)
      val modifiedGame = game.changePlayerName(game.players.head, "TesterXml")
      
      val fileIO = new TestXmlFileIO
      fileIO.save(modifiedGame)
      
      val loadedGame = fileIO.load()
      
      loadedGame.playerCount shouldBe modifiedGame.playerCount
      loadedGame.players.head.name shouldBe "TesterXml"
      loadedGame.currentPlayerIndex shouldBe modifiedGame.currentPlayerIndex
      
      cleanUp()
    }
  }
}
