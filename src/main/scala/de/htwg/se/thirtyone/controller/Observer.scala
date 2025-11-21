package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model.GameEvent

trait Observer {
    def update(event: GameEvent): Unit
}
