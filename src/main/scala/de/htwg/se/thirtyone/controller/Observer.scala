package de.htwg.se.thirtyone.controller

import de.htwg.se.thirtyone.model._

trait Observer {
    def update(event: GameEvent): Unit
}
