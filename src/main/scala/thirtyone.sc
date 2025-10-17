def cardTUI(x:Int): Unit = {
  val strTB = "+" + ("-" * x) + "+"
  val strMid = {
    "|" + (" " * x) + "|"
  }
  println(strTB)
  println(strMid)
  println(strMid)
  println(strMid)
  println(strMid)
  println(strMid)
  println(strTB)
}

cardTUI(20)