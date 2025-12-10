package de.htwg.se.thirtyone.controller.chainOfResponsibility

import de.htwg.se.thirtyone.controller.GameController
import de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.{BoundsHandler, PerformSwapHandler, PresenceHandler, SwapHandler}

object SwapProcessor:
  def swapChain: SwapHandler =
    BoundsHandler(Some(PresenceHandler(Some(PerformSwapHandler(None)))))

  def process(c: GameController, give: String, receive: String, chain: SwapHandler = swapChain): Result[GameController] =
    chain.handle(c, give, receive)