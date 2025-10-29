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

cardTUI(2, 1)

val cardC: Card = Card(5)

val tab: Table = Table(3, 5)
//tab.set(1,1, cardC.card)
print(tab)