package de.htwg.se.thirtyone.aview

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import javax.swing.SwingUtilities

class GUIClickSpec extends AnyWordSpec with Matchers {
  "GUI interactions" should {
    def waitForGrid(gui: GUI, timeoutMs: Long = 2000): Unit = {
      val deadline = System.currentTimeMillis() + timeoutMs
      while (gui.cardGrid.contents.isEmpty && System.currentTimeMillis() < deadline) Thread.sleep(10)
      gui.cardGrid.contents.nonEmpty shouldBe true
    }

    "call controller.selectNumber when clicking own card in give mode" in {
      if (java.awt.GraphicsEnvironment.isHeadless) cancel("Skipping GUI test in headless mode")
      var selected: String = ""
      class TestController(state: de.htwg.se.thirtyone.controller.state.ControllerState, gd: GameData) extends GameController(state, gd) {
        override def selectNumber(idx: String): Unit = selected = idx
      }

      val controller = new TestController(de.htwg.se.thirtyone.controller.state.PlayingState, GameData(2))
      val gui = new GUI(controller)

      // ensure table is drawn
      SwingUtilities.invokeAndWait(() => gui.update(PrintTable))
      waitForGrid(gui)

      // find a card that is in player's hand
      val playerHand = controller.gameData.table.getAll(controller.gameData.currentPlayerIndex + 1)
      playerHand.nonEmpty shouldBe true
      val card = playerHand.head
      val btnText = card.value + card.symbol

      // find matching button in grid
      val btnOpt = gui.cardGrid.contents.collect { case b: scala.swing.Button => b }.find(_.text == btnText)
      btnOpt.nonEmpty shouldBe true
      val btn = btnOpt.get

      // set give mode and click
      SwingUtilities.invokeAndWait(() => gui.swapMode = "give")
      SwingUtilities.invokeAndWait(() => btn.peer.doClick())
      
      // wait for selection
      val deadline = System.currentTimeMillis() + 1000
      while (selected.isEmpty && System.currentTimeMillis() < deadline) Thread.sleep(10)

      // selectNumber should have been called with index (1..3)
      selected.nonEmpty shouldBe true
    }

    "show infoLabel when clicking unrelated card in none mode" in {
      if (java.awt.GraphicsEnvironment.isHeadless) cancel("Skipping GUI test in headless mode")
      class TestController(state: de.htwg.se.thirtyone.controller.state.ControllerState, gd: GameData) extends GameController(state, gd) {
        override def selectNumber(idx: String): Unit = ()
      }

      val controller = new TestController(de.htwg.se.thirtyone.controller.state.PlayingState, GameData(2))
      val gui = new GUI(controller)

      SwingUtilities.invokeAndWait(() => gui.update(PrintTable))
      waitForGrid(gui)

      // pick a button for a card that is on the table but not in player's hand
      val playerHand = controller.gameData.table.getAll(controller.gameData.currentPlayerIndex + 1)
      val tableCards = controller.gameData.table.getAll(0)
      // find a table card not in hand
      val otherOpt = tableCards.find(c => !playerHand.contains(c))
      val otherCard = otherOpt.getOrElse(tableCards.head)
      val btnText = otherCard.value + otherCard.symbol

      val btnOpt = gui.cardGrid.contents.collect { case b: scala.swing.Button => b }.find(_.text == btnText)
      btnOpt.nonEmpty shouldBe true
      val btn = btnOpt.get

      SwingUtilities.invokeAndWait(() => gui.swapMode = "none")
      SwingUtilities.invokeAndWait(() => btn.peer.doClick())
      
      val deadline = System.currentTimeMillis() + 1000
      while (!gui.infoLabel.text.contains("nicht deine Karte") && System.currentTimeMillis() < deadline) Thread.sleep(10)

      gui.infoLabel.text should include("nicht deine Karte")
    }
  }
}
