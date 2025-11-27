// scala
package de.htwg.se.thirtyone.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.GameData
import de.htwg.se.thirtyone.util._
import scala.collection.mutable.ArrayBuffer

class PlayingStateSpec extends AnyWordSpec with Matchers {
  "PlayingState" should {

    // shared helpers and fixtures
    val events = ArrayBuffer.empty[String]

    def stubGameData(
                      index: Int = 0,
                      passTo: Option[Int] = None,
                      knockTo: Option[Int] = None
                    ): GameData =
      new GameData(null, 3, List.empty, index, null, true, List.empty) {
        override def pass(): GameData =
          passTo.map(i => stubGameData(i, None, None)).getOrElse(this)
        override def knock(): GameData =
          knockTo.map(i => stubGameData(i, None, None)).getOrElse(this)
      }

    def makeController(gd: GameData): GameController =
      new GameController(PlayingState, gd) {
        override def notifyObservers(event: GameEvent): Unit = events += event.toString
      }

    "execute passen" in {
      events.clear()
      val controller = makeController(stubGameData())
      val current = controller.gameData.currentPlayerIndex + 1

      PlayingState.execute("passen", controller)

      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(_.contains(s"PlayerPassed($current)")) shouldBe true
      events.exists(_.contains(s"RunningGame(${controller.gameData.currentPlayerIndex + 1})")) shouldBe true
    }

    "execute klopfen" in {
      events.clear()
      val controller = makeController(stubGameData())
      val current = controller.gameData.currentPlayerIndex + 1

      PlayingState.execute("klopfen", controller)

      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(_.contains(s"PlayerKnocked($current)")) shouldBe true
      events.exists(_.contains(s"RunningGame(${controller.gameData.currentPlayerIndex + 1})")) shouldBe true
    }

    "execute tauschen" in {
      events.clear()
      val controller = makeController(stubGameData())
      val current = controller.gameData.currentPlayerIndex + 1

      PlayingState.execute("tauschen", controller)

      controller.state should not be PlayingState
      events.exists(_.contains(s"PlayerSwapGive($current)")) shouldBe true
    }

    "handle invalid input" in {
      events.clear()
      val controller = makeController(stubGameData())

      PlayingState.execute("unbekannt", controller)

      events.exists(_.contains("InvalidInput")) shouldBe true
    }
  }
}
