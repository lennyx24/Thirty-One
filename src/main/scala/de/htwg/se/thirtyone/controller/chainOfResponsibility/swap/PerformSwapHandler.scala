package de.htwg.se.thirtyone.controller.chainOfResponsibility.swap

import de.htwg.se.thirtyone.controller.GameController
import de.htwg.se.thirtyone.controller.chainOfResponsibility._

case class PerformSwapHandler(override val next: Option[SwapHandler] = None) extends SwapHandler(next):
  override def handle(c: GameController, give: String, receive: String): Result[GameController] = {
    
    c.gameData = c.gameData.swap(c.gameData, c.gameData.currentPlayerIndex + 1, give, receive)
    Success(c)
  }
