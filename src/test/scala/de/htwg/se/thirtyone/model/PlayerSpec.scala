package de.htwg.se.thirtyone.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.thirtyone.model.gameImplementation.Player

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
  }
}
