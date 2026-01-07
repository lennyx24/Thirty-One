package de.htwg.se.thirtyone.controller.chainOfResponsibility

import de.htwg.se.thirtyone.controller.chainOfResponsibility.swap.{BoundsHandler, PerformSwapHandler, PresenceHandler, SwapHandler}
import scala.util._
import de.htwg.se.thirtyone.controller.ControllerInterface

object SwapProcessor:
  def swapChain: SwapHandler =
    BoundsHandler(Some(PresenceHandler(Some(PerformSwapHandler(None)))))

  def process(c: ControllerInterface, give: String, receive: String, chain: SwapHandler = swapChain): Try[ControllerInterface] =
    chain.handle(c, give, receive)