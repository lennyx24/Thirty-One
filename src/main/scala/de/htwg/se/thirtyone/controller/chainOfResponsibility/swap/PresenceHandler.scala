package de.htwg.se.thirtyone.controller.chainOfResponsibility.swap

import de.htwg.se.thirtyone.controller.ControllerInterface
import scala.util._

case class PresenceHandler(override val next: Option[SwapHandler] = None) extends SwapHandler(next):
  override def handle(c: ControllerInterface, give: String, receive: String): Try[ControllerInterface] =
    resolvePositions(c, give, receive).flatMap { case (pos1, pos2) =>
      val (r1, c1) = pos1
      val (r2, c2) = pos2
      val cell1 = c.gameData.table.grid(r1)(c1)
      val cell2 = c.gameData.table.grid(r2)(c2)
      (cell1, cell2) match
        case (Some(_), Some(_)) => passNext(c, give, receive)
        case _ => Failure(new IndexOutOfBoundsException("Eine oder beide Zellen sind leer"))
    }
