package de.htwg.se.thirtyone.controller.command

class UndoManager {
  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil

  def doStep(command: Command) = {
    command.doStep()
    undoStack = command::undoStack
    redoStack = Nil
  }

  def undoStep() = {
    undoStack match {
      case Nil =>
      case head::stack =>
        head.undoStep()
        undoStack = stack
        redoStack = head::redoStack
    }
  }
  def redoStep() = {
    redoStack match {
      case Nil =>
      case head::stack =>
        head.redoStep()
        redoStack = stack
        undoStack = head::undoStack
    }
  }
}
