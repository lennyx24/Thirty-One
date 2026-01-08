package de.htwg.se.thirtyone.controller.chainOfResponsibility.swap

import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.controller.chainOfResponsibility.*

import scala.util.*

case class PresenceHandler(override val next: Option[SwapHandler] = None) extends SwapHandler(next):
  override def handle(c: ControllerInterface, give: String, receive: String): Try[ControllerInterface] =
    val (pos1, pos2) = give match
      case "alle" =>
        val p1 = c.gameData.cardPositions(c.gameData.currentPlayerIndex + 1)(0)
        val p2 = c.gameData.cardPositions(c.gameData.currentPlayerIndex + 1)(2)
        (p1, p2)
      case _ =>
        val p1 = c.gameData.cardPositions(c.gameData.currentPlayerIndex + 1)(give.toInt - 1)
        val p2 = c.gameData.cardPositions(0)(receive.toInt - 1)
        (p1, p2)

    val (r1, c1) = pos1
    val (r2, c2) = pos2
    val cell1 = c.gameData.table.grid(r1)(c1)
    val cell2 = c.gameData.table.grid(r2)(c2)
    (cell1, cell2) match
      case (Some(_), Some(_)) => passNext(c, give, receive)
      case _ => Failure(new IndexOutOfBoundsException("Eine oder beide Zellen sind leer"))
