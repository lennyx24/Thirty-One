package de.htwg.se.thirtyone.controller.chainOfResponsibility.swap

import de.htwg.se.thirtyone.controller.ControllerInterface
import scala.util._

case class BoundsHandler(override val next: Option[SwapHandler] = None) extends SwapHandler(next):
  override def handle(c: ControllerInterface, give: String, receive: String): Try[ControllerInterface] =
    resolvePositions(c, give, receive).flatMap { case (pos1, pos2) =>
      val table = c.gameData.table
      val (r1, c1) = pos1
      val (r2, c2) = pos2
      val isOutOfBounds =
        r1 < 0 || r1 >= table.height || r2 < 0 || r2 >= table.height ||
          c1 < 0 || c1 >= table.width || c2 < 0 || c2 >= table.width

      if isOutOfBounds then
        Failure(new IndexOutOfBoundsException("Position ist außerhalb des Spielfelds"))
      else
        passNext(c, give, receive)
    }
