 package de.htwg.se.thirtyone.controller

 import org.scalatest.wordspec.AnyWordSpec
 import org.scalatest.matchers.should.Matchers.*

 import de.htwg.se.thirtyone.model._

 class GameControllerSpec extends AnyWordSpec {
   "GameController" should {
     "be able to calculate index" in {
       val dummyState = GameState(Table(), 3, 1, Deck(), false, Nil)
       val indexToGive = "1"
       GameController(dummyState).calculateIndex(indexToGive) should be (0)
       val indexToGive2 = "2"
       GameController(dummyState).calculateIndex(indexToGive2) should be(1)
       val indexToGive3 = "3"
       GameController(dummyState).calculateIndex(indexToGive3) should be(2)
     }
   }
 }
