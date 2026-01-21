package de.htwg.se.thirtyone.controller.implementation

import com.google.inject.Inject
import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.controller.state.{ControllerState, PlayingState, SwapState}
import de.htwg.se.thirtyone.model.GameInterface
import de.htwg.se.thirtyone.model.game.{GameData, Player}
import de.htwg.se.thirtyone.util._
import de.htwg.se.thirtyone.util.fileio.FileIO

class GameController @Inject() (
  var state: ControllerState,
  var gameData: GameInterface,
  val undoManager: UndoManager,
  val fileIO: FileIO
) extends ControllerInterface:

  override def handleInput(input: String): Unit = state.handleInput(input, this)

  override def pass(): Unit = state.pass(this)

  override def knock(): Unit = state.knock(this)

  override def swap(): Unit = state.swap(this)

  override def initialGame(idx: String, playerNames: List[String]): Unit =
    state.setupGame(idx.toInt, playerNames, this)

  override def changePlayerName(player: Player, newName: String): Unit =
    gameData = gameData.changePlayerName(player, newName)

  override def loadGame(): Unit =
    val game = fileIO.load()
    game match
      case gd: GameInterface =>
        this.gameData = gd
        if game.gameRunning then setState(PlayingState)
        notifyObservers(GameStarted)
        notifyObservers(PrintTable)

  override def saveGame(): Unit =
    fileIO.save(gameData)

  override def selectNumber(idx: String): Unit = state.selectNumber(idx, this)

  override def selectAll(): Unit = state.selectAll(this)

  override def countPoints(c: ControllerInterface, currentPlayer: Player): Unit =
    gameData = c.gameData.calculatePlayerPoints(currentPlayer)

  override def setState(controllerState: ControllerState): Unit = state = controllerState

  override def gameDataSetup(idx: String): Unit = gameData = GameData(idx.toInt)

  override def dealDamage(worstPlayer: Player): Unit = { 
    gameData = gameData.doDamage(worstPlayer)
    notifyObservers(RoundEnded(PlayerInfo(worstPlayer.id, worstPlayer.name)))
  }

  override def resetGame(): Unit = gameData = gameData.resetNewRound()

  override def setGameData(g: GameInterface): Unit = gameData = g

  override def gamePass(): Unit = gameData = gameData.pass()

  override def gameKnock(): Unit = gameData = gameData.knock()

  override def undo(): Unit =
    undoManager.undoStep()
    notifyObservers(PrintTable)
    state match
      case _: SwapState =>
        notifyObservers(PlayerSwapGive(PlayerInfo(gameData.currentPlayer.id, gameData.currentPlayer.name)))
      case _ =>
        notifyObservers(RunningGame(PlayerInfo(gameData.currentPlayer.id, gameData.currentPlayer.name)))

  override def redo(): Unit =
    undoManager.redoStep()
    notifyObservers(PrintTable)
    state match
      case _: SwapState =>
        notifyObservers(PlayerSwapGive(PlayerInfo(gameData.currentPlayer.id, gameData.currentPlayer.name)))
      case _ =>
        notifyObservers(RunningGame(PlayerInfo(gameData.currentPlayer.id, gameData.currentPlayer.name)))
