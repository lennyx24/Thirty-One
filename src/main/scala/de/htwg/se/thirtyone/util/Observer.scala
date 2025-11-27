package de.htwg.se.thirtyone.util


trait Observer {
    def update(event: GameEvent): Unit
}
