package de.htwg.se.thirtyone

import com.google.inject.AbstractModule
import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.controller.implementation.GameController
import de.htwg.se.thirtyone.controller.state.{ControllerState, SetupState}
import de.htwg.se.thirtyone.model.GameInterface
import de.htwg.se.thirtyone.model.factory.StandardGameFactory
import de.htwg.se.thirtyone.util.fileio.FileIO
import de.htwg.se.thirtyone.util.fileio.implementation.JsonFileIO
import net.codingwell.scalaguice.ScalaModule

class ThirtyOneModule extends AbstractModule with ScalaModule:
  override def configure(): Unit =
    bind(classOf[ControllerInterface]).to(classOf[GameController])

    bind(classOf[ControllerState]).toInstance(SetupState)

    bind(classOf[UndoManager]).asEagerSingleton()

    bind(classOf[GameInterface]).toInstance(StandardGameFactory.createGame(0))

    // bind(classOf[FileIO]).to(classOf[XmlFileIO])
    bind(classOf[FileIO]).to(classOf[JsonFileIO])
