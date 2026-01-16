package de.htwg.se.thirtyone.model.gameImplementation

case class InvisibleCard(size: Int = 10) {
  def invCell: String = " " * size + "   "

  override def toString: String = {
    invCell + "\n" +
      (invCell + "\n") * (size / 2) +
      invCell + "\n"
  }
}
