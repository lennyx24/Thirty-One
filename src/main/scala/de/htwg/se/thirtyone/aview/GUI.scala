package de.htwg.se.thirtyone.aview

import de.htwg.se.thirtyone.controller._
import de.htwg.se.thirtyone.util._

import java.awt.Color
import scala.swing._
import scala.swing.event._
import de.htwg.se.thirtyone.model.game.Player

class GUI(controller: ControllerInterface) extends Frame with Observer {
  title = "Thirty-One"
  peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)

  private enum SwapMode {
    case None, Give, Take
  }

  private val minPlayers = 2
  private val maxPlayers = 4

  private var swapMode: SwapMode = SwapMode.None

  val nameP1 = new TextField("Player 1")
  val nameP2 = new TextField("Player 2")
  val nameP3 = new TextField("Player 3")
  val nameP4 = new TextField("Player 4")
  val playerCountField = new TextField(minPlayers.toString)
  val nameFields = Vector(nameP1, nameP2, nameP3, nameP4)

  private def clampPlayerCount(count: Int): Int =
    math.max(minPlayers, math.min(maxPlayers, count))

  private def setPlayerCount(count: Int): Unit =
    val clamped = clampPlayerCount(count)
    playerCountField.text = clamped.toString
    nameP3.visible = clamped >= 3
    nameP4.visible = clamped >= 4

  val setupPanel = new BoxPanel(Orientation.Vertical) {
    border = Swing.EmptyBorder(15, 15, 15, 15)
      contents += new GridPanel(1, 1) {
        contents += new Label("Willkommen zu Schwimmen!")
      }
    contents += Swing.VStrut(20)

    contents += new GridPanel(3, 2) {
      contents += new Label("Wie viele Spieler seit ihr?")

      listenTo(playerCountField)
      reactions += {
        case EditDone(`playerCountField`) =>
          val parsed = playerCountField.text.toIntOption.getOrElse(minPlayers)
          setPlayerCount(parsed)
      }
      val buttonAdd = new Button("▲")
      val buttonSub = new Button("▼")
      contents += new GridPanel(1, 3) {
        contents += playerCountField
        contents += new GridPanel(2, 1) {
          contents += buttonAdd
          contents += buttonSub
        }
      }
      listenTo(buttonAdd, buttonSub)
      reactions += {
        case ButtonClicked(`buttonAdd`) =>
          val current = playerCountField.text.toIntOption.getOrElse(minPlayers)
          setPlayerCount(current + 1)
        case ButtonClicked(`buttonSub`) =>
          val current = playerCountField.text.toIntOption.getOrElse(minPlayers)
          setPlayerCount(current - 1)
      }
      contents += new Label("Namen der Spieler: ")

      contents += new GridPanel(playerCountField.text.toInt, 2) {
        contents += nameP1
        contents += nameP2
        contents += nameP3
        contents += nameP4
      }
      nameP3.visible = false
      nameP4.visible = false

      val loadGameButton = new Button("Load saved Game")
      contents += loadGameButton
      val start = new Button("Spiel starten")
      contents += start

      listenTo(start,loadGameButton)
      reactions += {
        case ButtonClicked(`start`) =>
          val count = playerCountField.text.toIntOption.getOrElse(minPlayers)
          val currentNames = nameFields.take(count).map(_.text).toList
          controller.initialGame(count.toString, currentNames)
        case ButtonClicked(`loadGameButton`) => controller.loadGame()
      }
      hGap = 5
      vGap = 5
    }
  }

  val infoLabel = new Label("Spiel beginnt...") {
    xLayoutAlignment = 0.5
  }

  private def findPlayer(info: PlayerInfo): Option[Player] =
    controller.gameData.players.find(_.id == info.id)

  val p1NameL = new Label("Player 1")
  val p1LivesL = new Label("Leben: ")
  val p1PointsL = new Label("Punkte: ")

  val p2NameL = new Label("Player 2")
  val p2LivesL = new Label("Leben: ")
  val p2PointsL = new Label("Punkte: ")

  val p3NameL = new Label("Player 3")
  val p3LivesL = new Label("Leben: ")
  val p3PointsL = new Label("Punkte: ")

  val p4NameL = new Label("Player 4")
  val p4LivesL = new Label("Leben: ")
  val p4PointsL = new Label("Punkte: ")

  val nameLabels = Vector(p1NameL, p2NameL, p3NameL, p4NameL)
  val livesLabels = Vector(p1LivesL, p2LivesL, p3LivesL, p4LivesL)
  val pointsLabels = Vector(p1PointsL, p2PointsL, p3PointsL, p4PointsL)

  val swapAllButton = new Button("Alles tauschen") {
    visible = false
  }

  val cardGrid = new GridPanel(3, 9) {
    vGap = 5
    hGap = 5
    preferredSize = new Dimension(600, 300)
  }

  val playingPanel = new BoxPanel(Orientation.Vertical) {
    contents += infoLabel

    contents += Swing.VStrut(25)

    val saveButton = new Button("save")
    val undoButton = new Button("<")
    val redoButton = new Button(">")
    val passButton = new Button("Passen")
    val knockButton = new Button("Klopfen")
    val swapButton = new Button("Tauschen")

    contents += new GridPanel(1, 9) {
      contents += new Label("")
      contents += p1NameL
      contents += p1LivesL
      contents += p1PointsL
      contents += new Label("")
      contents += p2NameL
      contents += p2LivesL
      contents += p2PointsL
      contents += new Label("")
    }

    contents += cardGrid

    contents += new GridPanel(2, 1) {
      contents += new GridPanel(1, 9) {
        contents += new Label("")
        contents += p4NameL
        contents += p4LivesL
        contents += p4PointsL
        contents += new Label("")
        contents += p3NameL
        contents += p3LivesL
        contents += p3PointsL
        contents += new Label("")
      }

      contents += new FlowPanel {
        contents += undoButton
        contents += redoButton
        contents += saveButton
        contents += passButton
        contents += knockButton
        contents += swapButton
        contents += swapAllButton
      }
    }

    listenTo(passButton, knockButton, swapButton, swapAllButton, saveButton, undoButton, redoButton)
    reactions += {
      case ButtonClicked(`passButton`) => controller.pass()
      case ButtonClicked(`knockButton`) => controller.knock()
      case ButtonClicked(`swapButton`) => controller.swap()
      case ButtonClicked(`swapAllButton`) => controller.selectAll()
      case ButtonClicked(`saveButton`) => controller.saveGame()
      case ButtonClicked(`undoButton`) => controller.undo()
      case ButtonClicked(`redoButton`) => controller.redo()
    }
  }

  private val winnerLabel = new Label("")
  var winningPlayer: Option[Player] = None

  val gameEndedPanel = new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(15, 15, 15, 15)
      val playAgainButton = new Button(" Nochmal spielen")
      val quitButton = new Button("Verlassen")
      
      contents += new GridPanel(1, 1) {
        contents += winnerLabel
      }

      contents += Swing.VStrut(25)

      contents += new GridPanel(2, 1) {
        contents += playAgainButton
        contents += quitButton
      }
      listenTo(playAgainButton, quitButton)
      reactions += {
        case ButtonClicked(`playAgainButton`) => controller.handleInput("j")
        case ButtonClicked(`quitButton`) => controller.handleInput("n") 
      }
  }

  def drawTable(): Unit =
    cardGrid.contents.clear()
    val gridData = controller.gameData.table.grid

    val playerHand = controller.gameData.getPlayersHand()
    val tableCards = controller.gameData.getTableCard()

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
                if swapMode == SwapMode.Give && handIndex != -1 then
                  controller.selectNumber((handIndex + 1).toString())
                else if swapMode == SwapMode.Take && tableIndex != -1 then
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

        for i <- 0 until 4 do
          if i < controller.gameData.playerCount then
            val player = controller.gameData.players(i)
            nameLabels(i).text = player.name
            livesLabels(i).text = s"Leben: ${controller.gameData.getPlayersHealth(player)}"
            pointsLabels(i).text = s"Punkte: ${controller.gameData.getPlayerScore(player)}"
            nameLabels(i).visible = true
            livesLabels(i).visible = true
            pointsLabels(i).visible = true
          else
            nameLabels(i).visible = false
            livesLabels(i).visible = false
            pointsLabels(i).visible = false

        repaint()
      })

    case RunningGame(playerInfo) =>
      infoLabel.text = s"${playerInfo.name} ist dran"
      swapAllButton.visible = false
      swapMode = SwapMode.None

    case PlayerScore(playerInfo) =>
      val playerIndex = controller.gameData.players.indexWhere(p => p.id == playerInfo.id)
      val points = findPlayer(playerInfo).map(controller.gameData.getPlayerScore).getOrElse(0.0)
      javax.swing.SwingUtilities.invokeLater(() => {
        if playerIndex >= 0 && playerIndex < nameLabels.length then
          pointsLabels(playerIndex).text = s"Punkte: $points"
          val player = controller.gameData.players(playerIndex)
          livesLabels(playerIndex).text = s"Leben: ${controller.gameData.getPlayersHealth(player)}"
          nameLabels(playerIndex).text = player.name
      })

    case PlayerSwapGive(playerInfo) =>
      infoLabel.text = s"${playerInfo.name}, wähle eine Karte zum abgeben"
      swapAllButton.visible = true
      swapMode = SwapMode.Give

    case PlayerSwapTake(playerInfo) =>
      infoLabel.text = s"${playerInfo.name}, wähle eine Karte zum nehmen"
      swapMode = SwapMode.Take

    case PlayerNameSet(index, name) =>
      javax.swing.SwingUtilities.invokeLater(() => {
        if index >= 1 && index <= nameFields.length then
          nameFields(index - 1).text = name
      })

    case GameEnded(winner) =>
      javax.swing.SwingUtilities.invokeLater(() => {
        findPlayer(winner).foreach(p => winningPlayer = Some(p))
        winnerLabel.text = s"Glückwunsch Spieler ${winner.name} du hast das Spiel gewonnen!"
        contents = gameEndedPanel
        pack()
        centerOnScreen()
        open()
      })

    case _ =>
}
