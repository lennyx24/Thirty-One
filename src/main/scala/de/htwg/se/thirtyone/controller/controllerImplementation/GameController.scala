package de.htwg.se.thirtyone.controller.controllerImplementation

import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.controller.state.*
import de.htwg.se.thirtyone.controller.*
import de.htwg.se.thirtyone.model.*
import de.htwg.se.thirtyone.util.*
import de.htwg.se.thirtyone.model.gameImplementation.GameData
import de.htwg.se.thirtyone.model.gameImplementation.Player
import com.google.inject.Inject
import de.htwg.se.thirtyone.fileio._
import de.htwg.se.thirtyone.fileio.implementation.XmlFileIO

class GameController @Inject() (var state: ControllerState, var gameData: GameInterface, val undoManager: UndoManager, val fileIO: FileIO) extends ControllerInterface:
  
  override def handleInput(input: String): Unit = state.handleInput(input, this)

  override def pass(): Unit = state.pass(this)

  override def knock(): Unit = state.knock(this)

  override def swap(): Unit = state.swap(this)

  override def initialGame(idx: String, playerNames: List[String]): Unit =
    selectNumber(idx)
    gameData = gameData.changePlayersNames(playerNames)

  override def loadGameXML(): Unit = {
    val game = fileIO.load()
    game match {
      case gd: GameInterface => 
        this.gameData = gd
        if(game.gameRunning) setState(PlayingState)
        notifyObservers(GameStarted)
        notifyObservers(PrintTable)
    }
  }

  override def saveGameXML(): Unit =
    fileIO.save(gameData)

  override def selectNumber(idx: String): Unit = state.selectNumber(idx, this)

  override def selectAll(): Unit = state.selectAll(this)

  override def countPoints(c: ControllerInterface, currentPlayer: Int): Unit = gameData = c.gameData.calculatePlayerPoints(currentPlayer)

  override def setState(controllerState: ControllerState): Unit = state = controllerState

  override def gameDataSetup(idx: String): Unit = gameData = GameData(idx.toInt)

  override def dealDamage(worstPlayer: Player): Unit = gameData = gameData.doDamage(worstPlayer)

  override def resetGame(): Unit = gameData = gameData.resetNewRound()

  override def setGameData(g: GameInterface): Unit = gameData = g

  override def gamePass(): Unit = gameData = gameData.pass()

  override def gameKnock(): Unit = gameData = gameData.knock()

  override def undo(): Unit =
    undoManager.undoStep()
    notifyObservers(PrintTable)
    notifyObservers(RunningGame(gameData.currentPlayer))

  override def redo(): Unit =
    undoManager.redoStep()
    notifyObservers(PrintTable)
    notifyObservers(RunningGame(gameData.currentPlayer))