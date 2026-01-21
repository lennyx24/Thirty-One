package de.htwg.se.thirtyone.controller.command

trait Command:
  def doStep(): Unit
  def undoStep(): Unit
  def redoStep(): Unit
