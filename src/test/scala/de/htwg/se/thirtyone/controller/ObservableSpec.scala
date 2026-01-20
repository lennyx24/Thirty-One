
package de.htwg.se.thirtyone.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.thirtyone.model._
import de.htwg.se.thirtyone.util._

class ObservableSpec extends AnyWordSpec with Matchers {

  "An Observable" should {
    "add a subscriber" in {
      val observable = new Observable
      val observer = new TestObserver
      observable.add(observer)
      observable.notifyObservers(GameStarted)
      observer.lastEvent shouldBe Some(GameStarted)
    }

    "remove a subscriber" in {
      val observable = new Observable
      val observer = new TestObserver
      observable.add(observer)
      observable.remove(observer)
      observable.notifyObservers(GameStarted)
      observer.lastEvent shouldBe None
    }
  }

  class TestObserver extends Observer {
    var lastEvent: Option[GameEvent] = None

    override def update(event: GameEvent): Unit = {
      lastEvent = Some(event)
    }
  }

}
