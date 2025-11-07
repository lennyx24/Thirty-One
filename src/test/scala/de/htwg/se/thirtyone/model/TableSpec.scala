package de.htwg.se.thirtyone.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class TableSpec extends AnyWordSpec{
  "Table" should {
    "have the format" in {
      val t1 = Table(1,1) should be (Table(1,1))
      val t2 = Table(2,2) should be (Table(2,2))
      val t3 = Table() should be (Table(3,9))
    }
    "be editable" in {
      val t1 = Table(1, 1)
      t1.set(0,0, Card('d', "4")) should be (Vector(Vector(Some(Card('d',"4",10)))))
      val t2 = Table(2, 2)
      t2.set(1, 1, Card('d', "4")) should be(Vector(Vector(None, None), Vector(None, Some(Card('d',"4",10)))))
      val t3 = Table(3, 3)
      t3.set(1, 1, Card('d', "4")) should be(Vector(Vector(None, None, None), Vector(None, Some(Card('d',"4",10)), None), Vector(None, None, None)))
    }
  }
}
