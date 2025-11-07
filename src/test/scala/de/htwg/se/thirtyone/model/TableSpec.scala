package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class TableSpec extends AnyWordSpec{
  "Table" should {
    "be editable" in {
      val tab: Table = Table()
      val h10: Card = Card('h', "10")
      val d4: Card = Card('d', "4")
      val s7: Card = Card('s', "7")

      val newTab1 = tab.set(0,1, h10)
      val s1 = newTab1.toString
      s1 should include ("h10")
      s1 should include ("+----------")
      s1 should not include ("d4")
      s1 should not include ("s7")

      val newTab2 = newTab1.set(0,2, d4)
      val s2 = newTab2.toString
      s2 should include ("h10")
      s2 should include ("d4")
      s2 should include ("+----------")
      s2 should not include ("s7")

      val newTab3 = newTab2.set(1,3,s7)
      val s3 = newTab3.toString
      s3 should include ("h10")
      s3 should include ("d4")
      s3 should include ("s7")
      s3 should include ("+----------")
    }
  }
}