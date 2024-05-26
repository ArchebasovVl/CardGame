import korlibs.image.bitmap.*
import korlibs.image.format.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.io.file.std.resourcesVfs


class DeskScene : Scene() {
    lateinit var hand: HandContainer
    lateinit var deskTop: CardContainer

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
        hand.alignBottomToBottomOf(this)
        addChild(hand)

        deskTop = CardContainer(null, colorToBitmap)
        deskTop.centerOn(this)
        addChild(deskTop)
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
        txtBottom.alignRightToRightOf(this, padding = 11)
        addChild(img)
        addChild(txtBottom)
        addChild(txtTop)
        if (card != null){
            changeCard(card)
        }
    }

    fun changeCard(card: Card) {
        print("Card is ${card.number} ")
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
    fun update(hand: MutableList<Card>) {
        var padding = 0
        for (card in hand) {
            val cardContainer = CardContainer(card, cardImages)
            cardContainer.xy(padding, 0)
            addChild(cardContainer)
            padding += 30
        }
    }
}
