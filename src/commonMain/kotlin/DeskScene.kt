import korlibs.image.bitmap.*
import korlibs.image.format.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.io.file.std.resourcesVfs
import kotlinx.coroutines.*


class DeskScene : Scene() {
    private val viewModel = ViewModel()
    private lateinit var hand: HandContainer
    private lateinit var deskTop: CardContainer

    override suspend fun SContainer.sceneMain() {
        val colorToBitmap = mapOf(
            "Back" to resourcesVfs["cardBack.png"].readBitmap(),
            "Blue" to resourcesVfs["cardBGb.png"].readBitmap(),
            "Green" to resourcesVfs["cardBGg.png"].readBitmap(),
            "Pink" to resourcesVfs["cardBGp.png"].readBitmap(),
            "Yellow" to resourcesVfs["cardBGy.png"].readBitmap()
        )

        hand = HandContainer(colorToBitmap)
        hand.centerXOnStage()
        hand.alignBottomToBottomOf(this, padding = -50)
        addChild(hand)

        deskTop = CardContainer(null, colorToBitmap)
        deskTop.centerOn(this)
        addChild(deskTop)

        launch {
            viewModel.handFlow.collect {
                hand.update(it)
                hand.centerXOnStage()
            }
        }
        launch {
            viewModel.deskTopFlow.collect {
                deskTop.changeCard(it)
            }
        }
        launch {
            viewModel.skipRound()
        }
    }
}

class CardContainer(
    card: Card?,
    private val cardImages: Map<String, Bitmap>
) : Container() {
    private var img = image(cardImages["Back"]!!) { scale = 0.04 }
    private val txtTop = text("")
    private val txtBottom = text("")

    init {
        img.centerOn(this)
        txtTop.alignTopToTopOf(this, padding = 8)
        txtTop.alignLeftToLeftOf(this, padding = 14)
        txtBottom.alignBottomToBottomOf(this, padding = 8)
        txtBottom.alignRightToRightOf(this, padding = 20)
        addChild(img)
        addChild(txtBottom)
        addChild(txtTop)
        if (card != null){
            changeCard(card)
        }
    }

    fun changeCard(card: Card) {
        img = image(cardImages[card.color]!!) { scale = 0.04 }
        txtTop.setText(card.number.toString())
        txtBottom.setText(card.number.toString())
        addChild(txtTop)
        addChild(txtBottom)
    }
}


class HandContainer(
    private val cardImages: Map<String, Bitmap>
) : Container() {

    init { text(" ") }

    fun update(hand: MutableList<Card>) {
        var padding = 10
        for (card in hand) {
            val cardContainer = CardContainer(card, cardImages)
            cardContainer.alignLeftToLeftOf(this, padding)
            cardContainer.alignBottomToBottomOf(this)
            addChild(cardContainer)
            padding += 30
        }
    }
}
