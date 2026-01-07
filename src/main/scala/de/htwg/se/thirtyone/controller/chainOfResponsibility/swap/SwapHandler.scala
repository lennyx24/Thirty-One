package de.htwg.se.thirtyone.controller.chainOfResponsibility.swap

import scala.util._
import de.htwg.se.thirtyone.controller.ControllerInterface

abstract class SwapHandler(val next: Option[SwapHandler] = None):
  protected def passNext(c: ControllerInterface, give: String, receive: String): Try[ControllerInterface] =
    next match
      case Some(n) => n.handle(c, give, receive)
      case None    => Failure(new IndexOutOfBoundsException("Kein Handler konnte den Zug ausführen"))

  def handle(c: ControllerInterface, give: String, receive: String): Try[ControllerInterface]
