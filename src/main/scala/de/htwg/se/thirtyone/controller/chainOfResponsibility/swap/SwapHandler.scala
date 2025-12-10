package de.htwg.se.thirtyone.controller.chainOfResponsibility.swap

import de.htwg.se.thirtyone.controller.GameController
import de.htwg.se.thirtyone.controller.chainOfResponsibility.{Failure, Result}

abstract class SwapHandler(val next: Option[SwapHandler] = None):
  protected def passNext(c: GameController, give: String, receive: String): Result[GameController] =
    next match
      case Some(n) => n.handle(c, give, receive)
      case None    => Failure("Kein Handler konnte den Zug ausführen")

  def handle(c: GameController, give: String, receive: String): Result[GameController]
