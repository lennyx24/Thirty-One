package de.htwg.se.thirtyone.controller.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._

import scala.collection.mutable.ArrayBuffer
import de.htwg.se.thirtyone.controller.state._
import de.htwg.se.thirtyone.controller.GameController

class PlayingStateSpec extends AnyWordSpec with Matchers {
  "PlayingState" should {

    val events = ArrayBuffer.empty[String]

    def stubGameData(
                      index: Int = 0,
                      passTo: Option[Int] = None,
                      knockTo: Option[Int] = None
                    ): GameData =
      new GameData(null, GameScoringStrategy.simpleScoringStrategy, 3, List(Player(false, 15.0, 3), Player(false, 10.0, 2), Player(false, 25.0, 1)), index, null, true, List.empty) {
        override def pass(): GameData =
          passTo.map(i => stubGameData(i, None, None)).getOrElse(this)
        override def knock(): GameData =
          knockTo.map(i => stubGameData(i, None, None)).getOrElse(this)
      }

    def makeController(gd: GameData): GameController = {
      val c = new GameController(PlayingState, gd)
      c.add(new Observer { override def update(e: GameEvent): Unit = events += e.toString })
      c
    }

    "execute passen" in {
      events.clear()
      val controller = makeController(stubGameData())
      val current = controller.gameData.currentPlayerIndex + 1

      PlayingState.pass(controller)

      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(_.contains(s"PlayerPassed($current)")) shouldBe true
      events.exists(_.contains(s"RunningGame(${controller.gameData.currentPlayerIndex + 1})")) shouldBe true
    }

    "execute klopfen" in {
      events.clear()
      val controller = makeController(stubGameData())
      val current = controller.gameData.currentPlayerIndex + 1

      PlayingState.knock(controller)

      events.exists(_.contains("PrintTable")) shouldBe true
      events.exists(_.contains(s"PlayerKnocked($current)")) shouldBe true
      events.exists(_.contains(s"RunningGame(${controller.gameData.currentPlayerIndex + 1})")) shouldBe true
    }

    "execute tauschen" in {
      events.clear()
      val controller = makeController(stubGameData())
      val current = controller.gameData.currentPlayerIndex + 1

      PlayingState.swap(controller)

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

