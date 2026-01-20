package de.htwg.se.thirtyone.util

class Observable:
  private var subscribers: Vector[Observer] = Vector.empty

  def add(observer: Observer): Unit = subscribers = subscribers :+ observer

  def remove(observer: Observer): Unit = subscribers = subscribers.filterNot(_ == observer)

  def notifyObservers(event: GameEvent): Unit = subscribers.foreach(_.update(event))
