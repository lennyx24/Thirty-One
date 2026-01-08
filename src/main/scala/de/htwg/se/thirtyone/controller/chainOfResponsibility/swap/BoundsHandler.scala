package de.htwg.se.thirtyone.controller.chainOfResponsibility.swap

import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.controller.chainOfResponsibility.*

import scala.util.*

case class BoundsHandler(override val next: Option[SwapHandler] = None) extends SwapHandler(next):
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
    val isBound = Try(r1 < 0 || r1 >= c.gameData.table.height || r2 < 0 || r2 >= c.gameData.table.height ||
      c1 < 0 || c1 >= c.gameData.table.width || c2 < 0 || c2 >= c.gameData.table.width)
    isBound match
      case Success(v) => passNext(c, give, receive)
      case Failure(e) => Failure(new IndexOutOfBoundsException("Position is out of bounds"))
