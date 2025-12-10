package de.htwg.se.thirtyone.controller.chainOfResponsibility.swap

import de.htwg.se.thirtyone.controller.chainOfResponsibility._

trait Result[+A]
case class Success[A](value: A) extends Result[A]
case class Failure(reason: String) extends Result[Nothing]
