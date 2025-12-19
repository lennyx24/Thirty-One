package de.htwg.se.thirtyone.aview

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._
import javax.swing.SwingUtilities

class GUISpec extends AnyWordSpec with Matchers {
  "GUI" should {
    "update RunningGame and PlayerScore and swap modes" in {
      if (java.awt.GraphicsEnvironment.isHeadless) cancel("Skipping GUI test in headless mode")
      val controller = new GameController(de.htwg.se.thirtyone.controller.state.PlayingState, GameData(2))
      val gui = new GUI(controller)

      SwingUtilities.invokeAndWait(() => gui.update(RunningGame(1)))
      gui.swapMode shouldBe "none"
      gui.infoLabel.text should include("Spieler 1")

      controller.gameData = controller.gameData.calculatePlayerPoints(1)
      SwingUtilities.invokeAndWait(() => gui.update(PlayerScore(1)))
      gui.scoreLabels(0).text should include("Punkte")

      SwingUtilities.invokeAndWait(() => gui.update(PlayerSwapGive(1)))
      gui.swapMode shouldBe "give"
      gui.swapAllButton.visible shouldBe true

      SwingUtilities.invokeAndWait(() => gui.update(PlayerSwapTake(1)))
      gui.swapMode shouldBe "take"
    }
  }
}

