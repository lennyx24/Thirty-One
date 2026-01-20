package de.htwg.se.thirtyone.controller.chainOfResponsibility.swap

import de.htwg.se.thirtyone.controller.ControllerInterface

import scala.util._

abstract class SwapHandler(val next: Option[SwapHandler] = None):
  protected def resolvePositions(c: ControllerInterface, give: String, receive: String): Try[((Int, Int), (Int, Int))] =
    Try {
      if give == "alle" then
        val playerRow = c.gameData.currentPlayerIndex + 1
        val p1 = c.gameData.cardPositions(playerRow)(0)
        val p2 = c.gameData.cardPositions(playerRow)(2)
        (p1, p2)
      else
        val giveIndex = give.toInt - 1
        val receiveIndex = receive.toInt - 1
        val p1 = c.gameData.cardPositions(c.gameData.currentPlayerIndex + 1)(giveIndex)
        val p2 = c.gameData.cardPositions(0)(receiveIndex)
        (p1, p2)
    }

  protected def passNext(c: ControllerInterface, give: String, receive: String): Try[ControllerInterface] =
    next match
      case Some(n) => n.handle(c, give, receive)
      case None => Failure(new IndexOutOfBoundsException("Kein Handler konnte den Zug ausführen"))

  def handle(c: ControllerInterface, give: String, receive: String): Try[ControllerInterface]
