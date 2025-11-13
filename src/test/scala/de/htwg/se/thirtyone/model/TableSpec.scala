package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class TableSpec extends AnyWordSpec{
  "Table" should {
    "be able to set its value" in {
      val tab: Table = Table()
      val h10: Card = Card('h', "10")
      val d4: Card = Card('d', "4")
      val s7: Card = Card('s', "7")

      val newTab1 = tab.set((0,1), h10)
      val s1 = newTab1.toString
      s1 should include ("h10")
      s1 should include ("+----------")
      s1 should not include ("d4")
      s1 should not include ("s7")

      val newTab2 = newTab1.set((0,2), d4)
      val s2 = newTab2.toString
      s2 should include ("h10")
      s2 should include ("d4")
      s2 should include ("+----------")
      s2 should not include ("s7")

      val newTab3 = newTab2.set((1,3),s7)
      val s3 = newTab3.toString
      s3 should include ("h10")
      s3 should include ("d4")
      s3 should include ("s7")
      s3 should include ("+----------")
    }
    "be able to get its value" in {
      val tab: Table = Table()
      val h10: Card = Card('h', "10")
      val d4: Card = Card('d', "4")
      val s7: Card = Card('s', "7")
      val newTab = tab.set((0,0),h10)
      val newTab2 = newTab.set((1,1),d4)
      val newTab3 = newTab2.set((2,0),s7)
      
      val c1 = newTab3.get((0,0))
      c1 should be(Card('h', "10",10))
      val c2 = newTab3.get((1,1))
      c2 should be(Card('d', "4",10))
      val c3 = newTab3.get((2, 0))
      c3 should be(Card('s', "7", 10))
    }
    "be able to swap cards" in {
      val tab: Table = Table()
      val h10: Card = Card('h', "10")
      val d4: Card = Card('d', "4")
      val s7: Card = Card('s', "7")
      val newTab = tab.set((0, 0), h10)
      val newTab2 = newTab.set((1, 1), d4)
      val newTab3 = newTab2.set((2, 0), s7)
      
      val newTab4 = newTab3.swap((0,0), (2,0))
      newTab4 should not be newTab3
      val newTab5 = newTab4.swap((0,0), (2,0))
      newTab5 should be (newTab3)
    }
    "be able to setAll cards" in {
      val tab: Table = Table()
      val h10: Card = Card('h', "10")
      val d4: Card = Card('d', "4")
      val s7: Card = Card('s', "7")

      val newTab = tab.setAll(List((0, 0), (0, 1), (1, 2)), List(h10, d4, s7))
      val s = newTab.toString
      s should include ("h10")
      s should include ("d4")
      s should include ("s7")
      s should include ("+----------")

      newTab.get((0, 0)) should be(Card('h', "10", 10))
      newTab.get((0, 1)) should be(Card('d', "4", 10))
      newTab.get((1, 2)) should be(Card('s', "7", 10))
    }
  }
}