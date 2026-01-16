package de.htwg.se.thirtyone.model.gameImplementation

import play.api.libs.json.{JsValue, Json}

import java.util.UUID
import scala.xml.{Elem, Node}

case class Player(
                   name: String = "Nameless Player",
                   hasKnocked: Boolean = false,
                   points: Double = 0,
                   playersHealth: Int = 3,
                   isAlive: Boolean = true,
                   hasPassed: Boolean = false,
                   id: String = UUID.randomUUID().toString
                 ):
  def receiveDamage(amount: Int): Player = if playersHealth > 1 then copy(playersHealth = playersHealth - 1) else copy(isAlive = false)

  def changeName(newName: String): Player = copy(name = newName)

  def toXml: Elem =
    <player>
      <name>{name}</name>
      <hasKnocked>{hasKnocked}</hasKnocked>
      <points>{points}</points>
      <playersHealth>{playersHealth}</playersHealth>
      <isAlive>{isAlive}</isAlive>
      <hasPassed>{hasPassed}</hasPassed>
      <id>{id}</id>
    </player>

  def toJson: JsValue = Json.obj(
    "name" -> name,
    "hasKnocked" -> hasKnocked,
    "points" -> points,
    "playersHealth" -> playersHealth,
    "isAlive" -> isAlive,
    "hasPassed" -> hasPassed,
    "id" -> id
  )

object Player:
  def fromXml(node: Node): Player =
    val name = (node \ "name").headOption.map(_.text).getOrElse("Nameless Player")
    val hasKnocked = (node \ "hasKnocked").headOption.map(_.text.toLowerCase == "true").getOrElse(false)
    val points = (node \ "points").headOption.map(_.text.toDouble).getOrElse(0.0)
    val playersHealth = (node \ "playersHealth").headOption.map(_.text.toInt).getOrElse(3)
    val isAlive = (node \ "isAlive").headOption.map(_.text.toLowerCase == "true").getOrElse(playersHealth > 0)
    val hasPassed = (node \ "hasPassed").headOption.map(_.text.toLowerCase == "true").getOrElse(false)
    val id = (node \ "id").headOption.map(_.text).filterNot(_.isEmpty).getOrElse(UUID.randomUUID().toString)
    Player(name, hasKnocked, points, playersHealth, isAlive, hasPassed, id)

  def fromJson(js: JsValue): Player =
    val name = (js \ "name").asOpt[String].getOrElse("Nameless Player")
    val hasKnocked = (js \ "hasKnocked").asOpt[Boolean].getOrElse(false)
    val points = (js \ "points").asOpt[Double].getOrElse(0.0)
    val playersHealth = (js \ "playersHealth").asOpt[Int].getOrElse(3)
    val isAlive = (js \ "isAlive").asOpt[Boolean].getOrElse(playersHealth > 0)
    val hasPassed = (js \ "hasPassed").asOpt[Boolean].getOrElse(false)
    val id = (js \ "id").asOpt[String].getOrElse(UUID.randomUUID().toString)
    Player(name, hasKnocked, points, playersHealth, isAlive, hasPassed, id)