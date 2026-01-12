package de.htwg.se.thirtyone.model.gameImplementation
import scala.xml.Elem

case class Player(
    name: String = "Nameless Player",
    hasKnocked: Boolean = false, 
    points: Double = 0,
    playersHealth: Int = 2,
    isAlive: Boolean = true,
    hasPassed:Boolean = false
):
    def receiveDamage(amount: Int): Player = if playersHealth > 1 then copy(playersHealth = playersHealth-1) else copy(isAlive = false)
    def changeName(newName: String): Player = copy(name = newName)
    def toXML: Elem =
      <Player>
        <name>{name}</name>
        <hasKnocked>{hasKnocked}</hasKnocked>
        <points>{points}</points>
        <playersHealth>{playersHealth}</playersHealth>
        <isAlive>{isAlive}</isAlive>
        <hasPassed>{hasPassed}</hasPassed>
      </Player>
      
object Player:
  def fromXML(node: xml.Node): Player =
    val name = (node \ "name").text
    val hasKnocked = (node \ "hasKnocked").text.toBoolean
    val points = (node \ "points").text.toDouble
    val playersHealth = (node \ "playersHealth").text.toInt
    val isAlive = (node \ "isAlive").text.toBoolean
    val hasPassed = (node \ "hasPassed").text.toBoolean
    Player(name, hasKnocked, points, playersHealth, isAlive, hasPassed)