package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.game.Player

class PlayerSpec extends AnyWordSpec with Matchers {
  "Player" should {
    "receive damage and stay alive if health > 1" in {
      val p = Player(playersHealth = 3, isAlive = true)
      val p2 = p.receiveDamage(1)
      p2.playersHealth shouldBe 2
      p2.isAlive shouldBe true
    }

    "receive damage and die if health == 0" in {
      val p = Player(playersHealth = 1, isAlive = true)
      val p2 = p.receiveDamage(1)
      p2.isAlive shouldBe false
    }

    "change name returns a new player with updated name" in {
      val p = Player(name = "Old")
      val p2 = p.changeName("New")
      p2.name shouldBe "New"
    }

    "serialize to and from XML" in {
      val p = Player(name = "Alice", hasKnocked = true, points = 12.0, playersHealth = 2, isAlive = true, hasPassed = true, id = "id-1")
      val xml = p.toXml
      val loaded = Player.fromXml(xml)
      loaded.name shouldBe p.name
      loaded.hasKnocked shouldBe p.hasKnocked
      loaded.points shouldBe p.points
      loaded.playersHealth shouldBe p.playersHealth
      loaded.isAlive shouldBe p.isAlive
      loaded.hasPassed shouldBe p.hasPassed
      loaded.id shouldBe p.id
    }

    "serialize to and from JSON" in {
      val p = Player(name = "Bob", hasKnocked = false, points = 8.0, playersHealth = 3, isAlive = true, hasPassed = false, id = "id-2")
      val json = p.toJson
      val loaded = Player.fromJson(json)
      loaded.name shouldBe p.name
      loaded.hasKnocked shouldBe p.hasKnocked
      loaded.points shouldBe p.points
      loaded.playersHealth shouldBe p.playersHealth
      loaded.isAlive shouldBe p.isAlive
      loaded.hasPassed shouldBe p.hasPassed
      loaded.id shouldBe p.id
    }

    "load defaults from empty XML" in {
      val xml = <player></player>
      val loaded = Player.fromXml(xml)
      loaded.name shouldBe "Nameless Player"
      loaded.points shouldBe 0.0
      loaded.playersHealth shouldBe 3
      loaded.isAlive shouldBe true
      loaded.hasPassed shouldBe false
    }

    "load defaults from empty JSON" in {
      val json = play.api.libs.json.Json.obj()
      val loaded = Player.fromJson(json)
      loaded.name shouldBe "Nameless Player"
      loaded.points shouldBe 0.0
      loaded.playersHealth shouldBe 3
      loaded.isAlive shouldBe true
      loaded.hasPassed shouldBe false
    }
  }
}
