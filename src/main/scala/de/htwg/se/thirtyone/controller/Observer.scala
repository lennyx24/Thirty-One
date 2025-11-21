package de.htwg.se.thirtyone.controller

trait Observer {
    def update(event: GameEvent): Unit
}
