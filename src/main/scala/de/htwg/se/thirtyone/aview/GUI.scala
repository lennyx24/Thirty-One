package de.htwg.se.thirtyone.aview

import scala.swing._
import de.htwg.se.thirtyone.controller.GameController

case class GUI(controller: GameController) extends Frame{
    title = "Thirty-One"
    contents = new FlowPanel {
        contents += new Button("Passen") {
            reactions += {
                case event.ButtonClicked(_) => controller.pass()
            }
        }
        contents += new Button("Klopfen") {
            reactions += {
                case event.ButtonClicked(_) => controller.knock()
            }
        }
        contents += new Button("Tauschen") {
            reactions += {
                case event.ButtonClicked(_) => controller.swap()
            }
        }
    }
    pack()
    centerOnScreen()
    open()
}
