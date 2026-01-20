package de.htwg.se.thirtyone.aview

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.implementation.GameController
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.model.game.{GameData, Player, Table}
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.util._

class GUISpec extends AnyWordSpec with Matchers {

  private def captureOut(f: => Unit): String = {
    val baos = new java.io.ByteArrayOutputStream()
    val ps = new java.io.PrintStream(baos)
    scala.Console.withOut(ps)(f)
    baos.toString
  }

  private def findButtons(c: scala.swing.Container): List[scala.swing.Button] =
    c.contents.toList.flatMap {
      case b: scala.swing.Button => List(b)
      case p: scala.swing.Container => findButtons(p)
      case _ => Nil
    }

  private def findTextFields(c: scala.swing.Container): List[scala.swing.TextField] =
    c.contents.toList.flatMap {
      case t: scala.swing.TextField => List(t)
      case p: scala.swing.Container => findTextFields(p)
      case _ => Nil
    }

  private def runOnEDT(f: => Unit): Unit =
    javax.swing.SwingUtilities.invokeAndWait(new Runnable { def run(): Unit = f })

  private def info(player: Player): PlayerInfo =
    PlayerInfo(player.id, player.name)

  "GUI" should {
    "drawTable and update paths should behave without errors" in {
      val base = GameData(2)
      val custom = base.copy(players = List(Player(name = "Alice"), Player(name = "Bob")))
      val controller = new GameController(PlayingState, custom, new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val gui = new de.htwg.se.thirtyone.aview.GUI(controller)

      gui.drawTable()
      gui.cardGrid.contents.size shouldBe 27
      gui.cardGrid.contents.exists(_.isInstanceOf[scala.swing.Button]) shouldBe true

      gui.update(RunningGame(info(controller.gameData.currentPlayer)))
      gui.infoLabel.text.toLowerCase should include ("dran")
      gui.swapAllButton.visible shouldBe false

      val swapPlayer = controller.gameData.currentPlayer
      gui.update(PlayerSwapGive(info(swapPlayer)))
      gui.swapAllButton.visible shouldBe true

      gui.update(PlayerSwapTake(info(swapPlayer)))

      val scorePlayer = controller.gameData.players(1)
      gui.update(PlayerScore(info(scorePlayer)))
      runOnEDT { }
      gui.pointsLabels(0).text should include ("Punkte")
    }

    "setup panel interactions (add/sub/start) and playing buttons call controller" in {
      import scala.collection.mutable.ArrayBuffer
      class SpyController(state: de.htwg.se.thirtyone.controller.state.ControllerState, gd: de.htwg.se.thirtyone.model.game.GameData) extends de.htwg.se.thirtyone.controller.implementation.GameController(state, gd, new de.htwg.se.thirtyone.controller.command.UndoManager(), de.htwg.se.thirtyone.StubFileIO) {
        val calls = ArrayBuffer.empty[String]
        override def selectNumber(idx: String): Unit = { calls += s"select:$idx"; super.selectNumber(idx) }
        override def pass(): Unit = { calls += "pass"; super.pass() }
        override def knock(): Unit = { calls += "knock"; super.knock() }
        override def swap(): Unit = { calls += "swap"; super.swap() }
        override def selectAll(): Unit = { calls += "selectAll"; super.selectAll() }
        override def initialGame(idx: String, playerNames: List[String]): Unit = { calls += s"init:${playerNames.mkString(",")}"; super.initialGame(idx, playerNames) }
      }

      val base = GameData(3)
      val custom = base.copy(
        players = List(Player(name = "Alice"), Player(name = "Bob"), Player(name = "Cara"))
      )
      val spy = new SpyController(SetupState, custom)
      val gui = new de.htwg.se.thirtyone.aview.GUI(spy)

      gui.update(GameStarted)
      runOnEDT { }

      val setup = gui.contents.head.asInstanceOf[scala.swing.Container]
      val buttons = findButtons(setup)
      val addOpt = buttons.find(_.text == "▲")
      val subOpt = buttons.find(_.text == "▼")
      val startOpt = buttons.find(_.text == "Spiel starten")
      addOpt.isDefined shouldBe true
      subOpt.isDefined shouldBe true
      startOpt.isDefined shouldBe true

      runOnEDT { addOpt.get.peer.doClick() }
      runOnEDT { }
      val tfields = findTextFields(setup)
      tfields.length should be >= 3

      runOnEDT { startOpt.get.peer.doClick() }
      spy.calls.exists(_.startsWith("init:")) shouldBe true

      gui.update(PrintTable)
      runOnEDT { }
      val play = gui.contents.head.asInstanceOf[scala.swing.Container]
      val playButtons = findButtons(play)
      val passBtn = playButtons.find(_.text == "Passen").get
      val knockBtn = playButtons.find(_.text == "Klopfen").get
      val swapBtn = playButtons.find(_.text == "Tauschen").get
      val swapAllBtn = playButtons.find(_.text == "Alles tauschen")

      runOnEDT { passBtn.peer.doClick() }
      runOnEDT { knockBtn.peer.doClick() }
      runOnEDT { swapBtn.peer.doClick() }
      swapAllBtn.foreach(b => runOnEDT { b.peer.doClick() })

      runOnEDT { }
      spy.calls should contain atLeastOneOf ("pass", "knock", "swap")
    }

    "drawTable colors and take/give click behavior" in {
      val controller = new GameController(PlayingState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val heart = de.htwg.se.thirtyone.model.game.Card('♥', "A")
      val spade = de.htwg.se.thirtyone.model.game.Card('♠', "K")
      val base = controller.gameData.asInstanceOf[de.htwg.se.thirtyone.model.game.GameData]
      // make cards visible to the current player: use positions that belong to player 1 (cardPositions(1))
      val t1 = base.table.set((0,1), heart).set((0,2), spade)
      controller.setGameData(base.copy(table = t1))

      val gui = new de.htwg.se.thirtyone.aview.GUI(controller)
      gui.drawTable()
      val heartBtn = gui.cardGrid.contents.collectFirst { case b: scala.swing.Button if b.text.contains("♥") => b }.get
      val spadeBtn = gui.cardGrid.contents.collectFirst { case b: scala.swing.Button if b.text.contains("♠") => b }.get
      import java.awt.Color
      heartBtn.foreground shouldBe Color.RED
      spadeBtn.foreground shouldBe Color.BLACK

      val calls = scala.collection.mutable.ArrayBuffer.empty[String]
      class Spy(state: de.htwg.se.thirtyone.controller.state.ControllerState, gd2: de.htwg.se.thirtyone.model.game.GameData) extends de.htwg.se.thirtyone.controller.implementation.GameController(state, gd2, new de.htwg.se.thirtyone.controller.command.UndoManager(), de.htwg.se.thirtyone.StubFileIO) {
        override def selectNumber(idx: String): Unit = { calls += idx; super.selectNumber(idx) }
      }

      val base2 = GameData(2)
      val t2 = base2.table.set((1,3), heart)
      val gd = base2.copy(table = t2)
      val spy = new Spy(PlayingState, gd)
      val gui2 = new de.htwg.se.thirtyone.aview.GUI(spy)
      gui2.drawTable()
      val btnOpt = gui2.cardGrid.contents.collectFirst { case b: scala.swing.Button if b.text.contains("A") && b.text.contains("♥") => b }
      btnOpt.isDefined shouldBe true
      val btn = btnOpt.get
      gui2.update(PlayerSwapTake(info(spy.gameData.currentPlayer)))
      javax.swing.SwingUtilities.invokeAndWait(new Runnable { def run(): Unit = btn.peer.doClick() })
      javax.swing.SwingUtilities.invokeAndWait(new Runnable { def run(): Unit = {} })
      calls.length should be >= 1
    }

    "setup EditDone clamps and name field visibility" in {
      val controller = new GameController(SetupState, GameData(4), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val gui = new de.htwg.se.thirtyone.aview.GUI(controller)
      gui.update(GameStarted)
      runOnEDT { }

      val setup = gui.contents.head.asInstanceOf[scala.swing.Container]
      val tfields = findTextFields(setup)
      val playerCount = tfields.find(_.text == "2").get

      playerCount.text = "4"
      playerCount.publish(scala.swing.event.EditDone(playerCount))
      playerCount.text shouldBe "4"

      val nameFields = tfields.tail
      nameFields.foreach { f =>
        f.text = "Max"
        f.publish(scala.swing.event.EditDone(f))
      }

      val startBtnOpt = findButtons(setup).find(_.text == "Spiel starten")
      startBtnOpt.isDefined shouldBe true
      runOnEDT { startBtnOpt.get.peer.doClick() }
      runOnEDT { }

      val playerNames = controller.gameData.asInstanceOf[de.htwg.se.thirtyone.model.game.GameData].players.map(_.name)
      playerNames.length shouldBe 4
      playerNames.forall(_ == "Max") shouldBe true
    }

    "handle Load, Save, Undo, Redo buttons" in {
      import scala.collection.mutable.ArrayBuffer
      class SpyController(state: de.htwg.se.thirtyone.controller.state.ControllerState, gd: de.htwg.se.thirtyone.model.game.GameData) extends de.htwg.se.thirtyone.controller.implementation.GameController(state, gd, new de.htwg.se.thirtyone.controller.command.UndoManager(), de.htwg.se.thirtyone.StubFileIO) {
        val calls = ArrayBuffer.empty[String]
        override def loadGame(): Unit = { calls += "load"; super.loadGame() }
        override def saveGame(): Unit = { calls += "save"; super.saveGame() }
        override def undo(): Unit = { calls += "undo"; super.undo() }
        override def redo(): Unit = { calls += "redo"; super.redo() }
      }

      val spy = new SpyController(SetupState, GameData(2))
      val gui = new de.htwg.se.thirtyone.aview.GUI(spy)
      
      gui.update(GameStarted)
      runOnEDT {}
      val setup = gui.contents.head.asInstanceOf[scala.swing.Container]
      val loadBtn = findButtons(setup).find(_.text == "Load saved Game").get
      runOnEDT { loadBtn.peer.doClick() }
      
      gui.update(PrintTable)
      runOnEDT {}
      val play = gui.contents.head.asInstanceOf[scala.swing.Container]
      val buttons = findButtons(play)
      val saveBtn = buttons.find(_.text == "save").get
      val undoBtn = buttons.find(_.text == "<").get
      val redoBtn = buttons.find(_.text == ">").get

      runOnEDT { saveBtn.peer.doClick() }
      runOnEDT { undoBtn.peer.doClick() }
      runOnEDT { redoBtn.peer.doClick() }

      spy.calls should contain allOf ("load", "save", "undo", "redo")
    }

    "handle Game Ended interactions" in {
      import scala.collection.mutable.ArrayBuffer
      class SpyController(state: de.htwg.se.thirtyone.controller.state.ControllerState, gd: de.htwg.se.thirtyone.model.game.GameData) extends de.htwg.se.thirtyone.controller.implementation.GameController(state, gd, new de.htwg.se.thirtyone.controller.command.UndoManager(), de.htwg.se.thirtyone.StubFileIO) {
        val calls = ArrayBuffer.empty[String]
        override def handleInput(input: String): Unit = { calls += s"input:$input"; super.handleInput(input) }
      }
      
      val spy = new SpyController(GameEndedState, GameData(2))
      val gui = new de.htwg.se.thirtyone.aview.GUI(spy)
      val winner = Player("Winner")
      
      gui.update(GameEnded(info(winner)))
      runOnEDT {}
      
      val endPanel = gui.contents.head.asInstanceOf[scala.swing.Container]
      val buttons = findButtons(endPanel)
      val playAgainBtn = buttons.find(_.text.trim == "Nochmal spielen").get
      val quitBtn = buttons.find(_.text == "Verlassen").get
      
      runOnEDT { playAgainBtn.peer.doClick() }
      runOnEDT { quitBtn.peer.doClick() }
      
      spy.calls should contain ("input:j")
      spy.calls should contain ("input:n")
    }

    "validate card clicks (wrong card / wrong mode)" in {
      val controller = new GameController(PlayingState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val base = controller.gameData.asInstanceOf[de.htwg.se.thirtyone.model.game.GameData]
      
      val posHand = base.cardPositions(1) 
      val posTable = base.cardPositions(0)
      
      val cardInHand = de.htwg.se.thirtyone.model.game.Card('♥', "7")
      val cardOnTable = de.htwg.se.thirtyone.model.game.Card('♠', "8")
      
      val t = base.table
        .set(posHand(0), cardInHand)
        .set(posTable(0), cardOnTable)
      
      val custom = base.copy(table = t)
      controller.setGameData(custom)
      
      val gui = new de.htwg.se.thirtyone.aview.GUI(controller)
      gui.drawTable()
      
      val btnHand = gui.cardGrid.contents.collectFirst { case b: scala.swing.Button if b.text.contains("♥") && b.text.contains("7") => b }.get
      val btnTable = gui.cardGrid.contents.collectFirst { case b: scala.swing.Button if b.text.contains("♠") && b.text.contains("8") => b }.get

      gui.update(PlayerSwapGive(info(controller.gameData.currentPlayer)))
      runOnEDT { btnTable.peer.doClick() }
      gui.infoLabel.text shouldBe "Das ist nicht deine Karte."
      
      gui.update(PlayerSwapTake(info(controller.gameData.currentPlayer)))
      runOnEDT { btnHand.peer.doClick() }
      gui.infoLabel.text shouldBe "Das ist nicht deine Karte."
      
      gui.update(RunningGame(info(controller.gameData.currentPlayer)))
      runOnEDT { btnHand.peer.doClick() }
      gui.infoLabel.text shouldBe "Das ist nicht deine Karte."
    }
    
    "handle PlayerNameSet and visibility logic in PrintTable" in {
       val controller = new GameController(SetupState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
       val gui = new de.htwg.se.thirtyone.aview.GUI(controller)
       
       gui.update(GameStarted)
       runOnEDT {}
       
       gui.update(PlayerNameSet(1, "UpdatedName"))
       runOnEDT {}
       
       gui.nameFields(0).text shouldBe "UpdatedName"
       
       gui.update(PrintTable)
       runOnEDT {}
       gui.nameLabels(0).visible shouldBe true
       gui.nameLabels(1).visible shouldBe true
       gui.nameLabels(2).visible shouldBe false
       gui.nameLabels(3).visible shouldBe false
    }
    
    "handle decrement button in setup" in {
       val controller = new GameController(SetupState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
       val gui = new de.htwg.se.thirtyone.aview.GUI(controller)
       gui.update(GameStarted)
       runOnEDT {}
       
       val setup = gui.contents.head.asInstanceOf[scala.swing.Container]
       val buttons = findButtons(setup)
       val addBtn = buttons.find(_.text == "▲").get
       val subBtn = buttons.find(_.text == "▼").get
       val countField = findTextFields(setup).find(_.text.matches("\\d")).get
       
       runOnEDT { subBtn.peer.doClick() }
       runOnEDT {}
       countField.text shouldBe "2"
       
       runOnEDT { addBtn.peer.doClick() }
       runOnEDT {}
       countField.text shouldBe "3"
       
       runOnEDT { subBtn.peer.doClick() }
       runOnEDT {}
       countField.text shouldBe "2"
    }
  }
}
