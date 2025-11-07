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



val tab: Table = Table()
val h10: Card = Card('h', "10")
val d4: Card = Card('d', "4")
val s7: Card = Card('s', "7")

val newTab1 = tab.set(0,1, h10)
val newTab2 = newTab1.set(0,2, d4)
val newTab3 = newTab2.set(0,3, s7)
val newTab4 = newTab3.set(1,0, s7)
val newTab5 = newTab4.set(1,2, s7)
val newTab6 = newTab5.set(1,4, s7)

print(newTab6)