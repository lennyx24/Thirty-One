package de.htwg.se.thirtyone.model.gameImplementation

import scala.xml.Elem

case class Card(symbol: Char, value: String, size: Int = 10) {
  require(size > 3)
  private val valueString: String = value + symbol.toString
  val cardString: String = cardSize

  def bar: String = "+" + ("-" * size) + "+ "

  def topCell: String = "| " + valueString + (" " * (size - valueString.length - 1)) + "| "

  def cells: String = "|" + (" " * (size)) + "| "

  def cardSize: String = {
    bar + "\n" +
      topCell + "\n" +
      (cells + "\n") * (size / 2 - 1) +
      bar + "\n"
  }

  def toXML: Elem =
    <card>
      <symbol>{ symbol }</symbol>
      <value>{ value }</value>
      <size>{ size }</size>
    </card>
}
object Card:
  def fromXML(node: xml.Node): Card =
    val symbol = (node \ "symbol").text.charAt(0)
    val value = (node \ "value").text
    val size = (node \ "size").text.toInt
    Card(symbol, value, size)