package de.htwg.se.thirtyone.model.gameImplementation
import play.api.libs.json.{JsValue, Json}

import scala.xml.Elem

import java.util.UUID

case class Player(
    name: String = "Nameless Player",
    hasKnocked: Boolean = false, 
    points: Double = 0,
    playersHealth: Int = 3,
    isAlive: Boolean = true,
    hasPassed:Boolean = false,
    id: String = UUID.randomUUID().toString
):
    def receiveDamage(amount: Int): Player = if playersHealth > 1 then copy(playersHealth = playersHealth-1) else copy(isAlive = false)
    def changeName(newName: String): Player = copy(name = newName)
    def toXML: Elem =
      <player>
        <name>{name}</name>
        <hasKnocked>{hasKnocked}</hasKnocked>
        <points>{points}</points>
        <playersHealth>{playersHealth}</playersHealth>
        <isAlive>{isAlive}</isAlive>
        <hasPassed>{hasPassed}</hasPassed>
      </player>
      
    def toJson: JsValue = Json.obj(
      "name" -> name,
      "hasKnocked" -> hasKnocked,
      "points" -> points,
      "playersHealth" -> playersHealth,
      "isAlive" -> isAlive,
      "hasPassed" -> hasPassed
    )
      
object Player:
  def fromXML(node: xml.Node): Player =
    val name = (node \ "name").text
    val hasKnocked = (node \ "hasKnocked").text.toBoolean
    val points = (node \ "points").text.toDouble
    val playersHealth = (node \ "playersHealth").text.toInt
    val isAlive = (node \ "isAlive").text.toBoolean
    val hasPassed = (node \ "hasPassed").text.toBoolean
    Player(name, hasKnocked, points, playersHealth, isAlive, hasPassed)
    
  def fromJson(js: JsValue): Player =
    val name = (js \ "name").as[String]
    val hasKnocked = (js \ "hasKnocked").as[Boolean]
    val points = (js \ "points").as[Double]
    val playersHealth = (js \ "playersHealth").as[Int]
    val isAlive = (js \ "isAlive").as[Boolean]
    val hasPassed = (js \ "hasPassed").as[Boolean]
    Player(name, hasKnocked, points, playersHealth, isAlive, hasPassed)