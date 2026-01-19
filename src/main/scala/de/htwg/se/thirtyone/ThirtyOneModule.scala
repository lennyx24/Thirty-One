package de.htwg.se.thirtyone

import de.htwg.se.thirtyone.model.GameInterface
import de.htwg.se.thirtyone.controller.*
import de.htwg.se.thirtyone.controller.controllerImplementation.*
import de.htwg.se.thirtyone.model.factory.StandardGameFactory
import de.htwg.se.thirtyone.controller.state.SetupState
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import de.htwg.se.thirtyone.controller.state.ControllerState
import de.htwg.se.thirtyone.util.fileio._
import de.htwg.se.thirtyone.util.fileio.implementation._

class ThirtyOneModule extends AbstractModule with ScalaModule{
    override def configure(): Unit =
        bind(classOf[ControllerInterface]).to(classOf[GameController])

        bind(classOf[ControllerState]).toInstance(SetupState)

        bind(classOf[UndoManager]).asEagerSingleton()

        bind(classOf[GameInterface]).toInstance(StandardGameFactory.createGame(0))
        
        //bind(classOf[FileIO]).to(classOf[XmlFileIO])
        bind(classOf[FileIO]).to(classOf[JsonFileIO])
}