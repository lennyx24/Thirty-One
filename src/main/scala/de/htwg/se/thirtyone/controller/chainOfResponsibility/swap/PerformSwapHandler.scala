package de.htwg.se.thirtyone.controller.chainOfResponsibility.swap

import de.htwg.se.thirtyone.controller.ControllerInterface
import de.htwg.se.thirtyone.controller.chainOfResponsibility.*

import scala.util.*

case class PerformSwapHandler(override val next: Option[SwapHandler] = None) extends SwapHandler(next):
  override def handle(c: ControllerInterface, give: String, receive: String): Try[ControllerInterface] = {

    c.gameData.swap(c.gameData.currentPlayerIndex + 1, give, receive).map { newGameData =>
      c.setGameData(newGameData)
      c
    }
  }
