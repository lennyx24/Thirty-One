package de.htwg.se.thirtyone.model

case class InvisibleCard(size: Int = 10) {
  def invCell: String = " " * size + "   "

  def invCard: String = {
    invCell + "\n" +
      (invCell + "\n") * (size / 2) +
      invCell + "\n"
  }
}
