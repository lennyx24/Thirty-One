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



val tab: Table = Table()
val h10: Card = Card('h', "10")
val d4: Card = Card('d', "4")
val s7: Card = Card('s', "7")

tab.set(0,1, h10)
tab.set(0,2, d4)
tab.set(0,3, s7)

Deck.size(10).foreach(println)