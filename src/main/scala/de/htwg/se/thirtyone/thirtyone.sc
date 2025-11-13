import de.htwg.se.thirtyone.model._

def cardTUI(x: Int, n: Int): Unit = {
  val topBot = "+" + ("-" * x) + "+"
  val mid = "|" + (" " * x) + "|"
  val gap = "  "
  val topRow = (topBot + gap) * n
  val midRow = (mid + gap) * n
  val midRows = (midRow + "\n") * (x / 2) + midRow
  val round = "-" * (x / 5) + " Round 1 " + "-" * (x / 5)

  println(" " * (x + 4) * (n / 2) + round)
  println(topRow)
  println(midRows)
  print(topRow)
}



var tab: Table = Table()
val h10: Card = Card('h', "10")
val d4: Card = Card('d', "4")
val s7: Card = Card('s', "7")

tab = tab.set(0,0,h10)
tab = tab.set(0,1,h10)
tab = tab.set(0,2,h10)
tab = tab.set(1,1,d4)
tab = tab.set(1,2,d4)
tab = tab.set(1,3,d4)
tab = tab.set(2,0,s7)
print(tab)

tab = tab.swap((0,1), (1,1))
print(tab)

var xtab: Table = Table()
val newTab = xtab.setAll((0, 0) :: (0, 1) :: (0, 2) :: Nil, List(Card('h', "10", 10), Card('d', "4", 10), Card('s', "7", 10)))
