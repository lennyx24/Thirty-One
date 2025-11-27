package de.htwg.se.thirtyone.util


class Observable {
    var subscribers: Vector[Observer] = Vector()
    def add(s: Observer): Unit = subscribers = subscribers :+ s
    def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)
    def notifyObservers(event: GameEvent): Unit = subscribers.foreach(o => o.update(event))
}
