package de.htwg.se.thirtyone.aview

import scala.swing._
import scala.swing.event._
import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.util._
import java.awt.Color
import de.htwg.se.thirtyone.controller.state.SwapState

class GUI(controller: GameController) extends Frame with Observer{
    title = "Thirty-One"
    peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)

    var swapMode: String = "none"

    val setupPanel = new BoxPanel(Orientation.Vertical) {
        contents += new GridPanel(2, 1) {
            contents += new Label("Willkommen zu Schwimmen!")
            contents += new Label("Wie viele Spieler seit ihr Heute?")
        }
        val b2 = new Button("2")
        val b3 = new Button("3")
        val b4 = new Button("4")
        contents += new FlowPanel {
            contents += b2
            contents += b3
            contents += b4      
        }

        listenTo(b2, b3, b4)
        reactions += {
            case ButtonClicked(b) => controller.selectNumber(b.text)
        }
    }

    val infoLabel = new Label("Spiel beginnt...") { xLayoutAlignment = 0.5 }
    val pointsPlayer1 = new Label("Punkte: ")
    val pointsPlayer2 = new Label("Punkte: ")
    val pointsPlayer3 = new Label("Punkte: ")
    val pointsPlayer4 = new Label("Punkte: ")

    val scoreLabels = Vector(pointsPlayer1, pointsPlayer2, pointsPlayer3, pointsPlayer4)

    val swapAllButton = new Button("Alles tauschen") {visible = false}

    val cardGrid = new GridPanel(3, 9) {
        vGap = 5
        hGap = 5
        preferredSize = new Dimension(600, 300)
    }

    val playingPanel = new BoxPanel(Orientation.Vertical) {
        contents += infoLabel

        contents += Swing.VStrut(25)

        val passButton = new Button("Passen")
        val knockButton = new Button("Klopfen")
        val swapButton = new Button("Tauschen")
        
        contents += new GridPanel(1, 5) {
            contents += new Label("Spieler 1")
            contents += pointsPlayer1
            contents += new Label("")
            contents += new Label("Spieler 2")
            contents += pointsPlayer2
        }

        contents += cardGrid

        contents += new GridPanel(2, 5) {
            contents += new Label("Spieler 4")
            contents += pointsPlayer4
            contents += new Label("")
            contents += new Label("Spieler 3")
            contents += pointsPlayer3

            contents += new Label("")
            contents += passButton
            contents += knockButton
            contents += swapButton
            contents += swapAllButton
        }

        listenTo(passButton, knockButton, swapButton, swapAllButton)
        reactions += {
            case ButtonClicked(`passButton`) => controller.pass()
            case ButtonClicked(`knockButton`) => controller.knock()
            case ButtonClicked(`swapButton`) => controller.swap()
            case ButtonClicked(`swapAllButton`) => controller.selectAll()
        }
    }

    def drawTable(): Unit =
        cardGrid.contents.clear()
        val gridData = controller.gameData.table.grid
        val currentPlayer = controller.gameData.currentPlayerIndex

        val playerHand = controller.gameData.table.getAll(currentPlayer + 1)
        val tableCards = controller.gameData.table.getAll(0)

        for (row <- 0 until 3) {
            for (col <- 0 until 9) {
                gridData(row)(col) match {
                    case Some(card) =>
                        val btn = new Button(card.value + card.symbol)
                        if (card.symbol == '♥' || card.symbol == '♦') {
                            btn.foreground = Color.RED
                        } else {
                            btn.foreground = Color.BLACK
                        }
                        
                        val handIndex = playerHand.indexOf(card)
                        val tableIndex = tableCards.indexOf(card)

                        listenTo(btn)
                        reactions += {
                            case ButtonClicked(`btn`) =>
                                if swapMode == "give" && handIndex != -1  then
                                    controller.selectNumber((handIndex + 1).toString())
                                else if swapMode == "take" && tableIndex != -1 then
                                    controller.selectNumber((tableIndex + 1).toString())
                                else
                                    infoLabel.text = "Das ist nicht deine Karte."

                        }
                        cardGrid.contents += btn
                    case None =>
                        val emptyLbl = new Label("")
                        cardGrid.contents += emptyLbl
                }
            }
        }
        cardGrid.revalidate()
        cardGrid.repaint()
    override def update(event: GameEvent): Unit = event match
        case GameStarted =>
            javax.swing.SwingUtilities.invokeLater(() => {
                contents = setupPanel
                pack()
                centerOnScreen()
                open()
            })
            
        case PrintTable =>
            javax.swing.SwingUtilities.invokeLater(() => {
                if contents.isEmpty || contents.head != playingPanel then
                    contents = playingPanel
                    pack()
                    centerOnScreen()
                drawTable()
                repaint()
            })

        case RunningGame(player) =>
            infoLabel.text = s"Spieler $player ist dran"
            swapAllButton.visible = false
            swapMode = "none"

        case PlayerScore(player) =>
            val points = controller.gameData.getPlayerPoints(player)
            if player >= 0 then
                scoreLabels(player-1).text = s"Punkte: $points"

        case PlayerSwapGive(player) =>
            infoLabel.text = s"Spieler $player, wähle eine Karte zum abgeben"
            swapAllButton.visible = true
            swapMode = "give"
            
        case PlayerSwapTake(player) =>
            infoLabel.text = s"Spieler $player, wähle eine Karte zum nehmen"
            swapMode = "take"
        case _ =>
}
