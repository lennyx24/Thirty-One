package de.htwg.se.thirtyone.aview

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller.controllerImplementation.GameController
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.model.gameImplementation.{GameData, Player, Table}
import de.htwg.se.thirtyone.controller.command.UndoManager
import de.htwg.se.thirtyone.util._

class GUISpec extends AnyWordSpec with Matchers {
  private val isCI: Boolean = sys.env.get("GITHUB_ACTIONS").isDefined || sys.env.get("CI").isDefined

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

  "GUI" should {
    "drawTable and update paths should behave without errors" in {
      if (isCI) cancel("Skipping GUI tests in CI (no display)")
      val base = GameData(2)
      val custom = base.copy(players = List(Player(name = "Alice"), Player(name = "Bob")))
      val controller = new GameController(PlayingState, custom, new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val gui = new de.htwg.se.thirtyone.aview.GUI(controller)

      gui.drawTable()
      gui.cardGrid.contents.size shouldBe 27
      gui.cardGrid.contents.exists(_.isInstanceOf[scala.swing.Button]) shouldBe true

      gui.update(RunningGame(controller.gameData.currentPlayer))
      gui.infoLabel.text.toLowerCase should include ("dran")
      gui.swapMode shouldBe "none"

      val swapPlayer = controller.gameData.currentPlayer
      gui.update(PlayerSwapGive(swapPlayer))
      gui.swapMode shouldBe "give"
      gui.swapAllButton.visible shouldBe true

      gui.update(PlayerSwapTake(swapPlayer))
      gui.swapMode shouldBe "take"

      val scorePlayer = controller.gameData.players(1)
      gui.update(PlayerScore(scorePlayer))
      runOnEDT { }
      gui.pointsLabels(0).text should include ("Punkte")
    }

    "setup panel interactions (add/sub/start) and playing buttons call controller" in {
      if (isCI) cancel("Skipping GUI tests in CI (no display)")
      import scala.collection.mutable.ArrayBuffer
      class SpyController(state: de.htwg.se.thirtyone.controller.state.ControllerState, gd: de.htwg.se.thirtyone.model.gameImplementation.GameData) extends de.htwg.se.thirtyone.controller.controllerImplementation.GameController(state, gd, new de.htwg.se.thirtyone.controller.command.UndoManager(), de.htwg.se.thirtyone.StubFileIO) {
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
      if (isCI) cancel("Skipping GUI tests in CI (no display)")
      val controller = new GameController(PlayingState, GameData(2), new UndoManager(), de.htwg.se.thirtyone.StubFileIO)
      val heart = de.htwg.se.thirtyone.model.gameImplementation.Card('♥', "A")
      val spade = de.htwg.se.thirtyone.model.gameImplementation.Card('♠', "K")
      val base = controller.gameData.asInstanceOf[de.htwg.se.thirtyone.model.gameImplementation.GameData]
      val t1 = base.table.set((0,0), heart).set((0,1), spade)
      controller.setGameData(base.copy(table = t1))

      val gui = new de.htwg.se.thirtyone.aview.GUI(controller)
      gui.drawTable()
      val heartBtn = gui.cardGrid.contents.collectFirst { case b: scala.swing.Button if b.text.contains("♥") => b }.get
      val spadeBtn = gui.cardGrid.contents.collectFirst { case b: scala.swing.Button if b.text.contains("♠") => b }.get
      import java.awt.Color
      heartBtn.foreground shouldBe Color.RED
      spadeBtn.foreground shouldBe Color.BLACK

      val calls = scala.collection.mutable.ArrayBuffer.empty[String]
      class Spy(state: de.htwg.se.thirtyone.controller.state.ControllerState, gd2: de.htwg.se.thirtyone.model.gameImplementation.GameData) extends de.htwg.se.thirtyone.controller.controllerImplementation.GameController(state, gd2, new de.htwg.se.thirtyone.controller.command.UndoManager(), de.htwg.se.thirtyone.StubFileIO) {
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
      gui2.swapMode = "take"
      javax.swing.SwingUtilities.invokeAndWait(new Runnable { def run(): Unit = btn.peer.doClick() })
      javax.swing.SwingUtilities.invokeAndWait(new Runnable { def run(): Unit = {} })
      calls.length should be >= 1
    }

    "setup EditDone clamps and name field visibility" in {
      if (isCI) cancel("Skipping GUI tests in CI (no display)")
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

      val playerNames = controller.gameData.asInstanceOf[de.htwg.se.thirtyone.model.gameImplementation.GameData].players.map(_.name)
      playerNames.length shouldBe 4
      playerNames.forall(_ == "Max") shouldBe true
    }
  }
}
