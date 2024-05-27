package View

import Card
import ViewModel
import korlibs.image.bitmap.*
import korlibs.image.format.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.input.*
import korlibs.korge.ui.*
import korlibs.math.geom.*
import kotlinx.coroutines.*


class DeskScene : Scene() {
    private val viewModel = ViewModel()
    private lateinit var hand: HandContainer
    private lateinit var botHand: BotHandContainer
    private lateinit var deskTop: CardContainer

    override suspend fun SContainer.sceneMain() {
        val colorToBitmap = mapOf(
            "Back" to resourcesVfs["cardBack.png"].readBitmap(),
            "Blue" to resourcesVfs["cardBGb.png"].readBitmap(),
            "Green" to resourcesVfs["cardBGg.png"].readBitmap(),
            "Pink" to resourcesVfs["cardBGp.png"].readBitmap(),
            "Yellow" to resourcesVfs["cardBGy.png"].readBitmap()
        )

        val skipButton = uiButton()
        skipButton.text = "Skip"
        skipButton.size = Size(60, 40)
        skipButton.alignRightToRightOf(this)
        skipButton.alignTopToTopOf(this, 100)
        skipButton.onPress{ launch { viewModel.skipRound() } }

        val deck = CardContainer(null, colorToBitmap)
        deck.alignRightToRightOf(this)
        deck.centerYOn(this)
        deck.onUp{ launch { viewModel.getCard() } }
        addChild(deck)

        hand = HandContainer(colorToBitmap, viewModel)
        hand.alignTopToTopOf(this, 150)
        hand.alignBottomToBottomOf(this, padding = -50)
        addChild(hand)

        deskTop = CardContainer(null, colorToBitmap)
        deskTop.centerOn(this)
        addChild(deskTop)

        botHand = BotHandContainer(colorToBitmap)
        botHand.centerXOnStage()
        botHand.alignTopToTopOf(this, padding = -50)
        addChild(botHand)

        launch {
            viewModel.handFlow.collect {
                hand.update(it)
                hand.centerXOnStage()
                if (it.size == 0){
                    val endLabel = text("Поздравляем, вы победили!")
                    endLabel.centerOnStage()
                    onClick{ views().gameWindow.exit() }
                }
            }
        }
        launch {
            viewModel.deskTopFlow.collect {
                deskTop.changeCard(it)
            }
        }
        launch {
            viewModel.botHandFlow.collect {
                botHand.update(it)
                botHand.centerXOnStage()
                if (it == 0) {
                    val endLabel = text("Поздравляем, вы проиграли!")
                    endLabel.centerOnStage()
                    onClick { views().gameWindow.exit() }
                }
            }
        }
        launch {
            viewModel.skipRound()
        }
    }
}


