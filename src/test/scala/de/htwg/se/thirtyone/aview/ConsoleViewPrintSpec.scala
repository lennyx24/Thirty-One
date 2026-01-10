package de.htwg.se.thirtyone.aview

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.thirtyone.controller.controllerImplementation.*
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.model.GameData
import de.htwg.se.thirtyone.util._

import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsoleViewPrintSpec extends AnyWordSpec with Matchers {
  "ConsoleView" should {
    val gameData = GameData(2)
    val controller = new GameController(PlayingState, gameData)
    val view = ConsoleView(controller)

    def capture(f: => Unit): String = {
      val out = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(out)) { f }
      out.toString
    }

    "print PlayerPassed message" in {
      val s = capture { view.update(PlayerPassed(1)) }
      s should include("Spieler 1 setzt diese Runde aus")
    }

    "print PlayerKnocked message" in {
      val s = capture { view.update(PlayerKnocked(2)) }
      s should include("Spieler 2 hat diese Runde geklopft")
    }

    "print PlayerSwapped message" in {
      val s = capture { view.update(PlayerSwapped(3)) }
      s should include("Spieler 3 tauscht diese Runde")
    }

    "print table for PrintTable event" in {
      val s = capture { view.update(PrintTable) }
      s should include("+")
    }
  }
}
