import de.htwg.model._

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



val tab: Table = Table(3, 5)
val h10: Card = Card(10)
val h8: Card = Card(10)
val h2: Card = Card(10)

tab.set(1,1, h10.card)
tab.set(1,2, h8.card)
tab.set(1,3, h2.card)
val cards = tab.cells(1)(1) + tab.cells(1)(2) + tab.cells(1)(3)
print(cards)