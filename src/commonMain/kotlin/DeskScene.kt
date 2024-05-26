import korlibs.image.bitmap.*
import korlibs.image.format.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.io.file.std.resourcesVfs


class DeskScene : Scene() {
    lateinit var hand: HandContainer

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

        val deskTop = CardContainer(null, colorToBitmap["Back"]!!)
        deskTop.centerOn(this)
        addChild(deskTop)
    }
}

class CardContainer(number: Int?, cardBitmap: Bitmap) : Container() {
    init {
        val img = image(cardBitmap) { scale = 0.04 }
        img.centerOn(this)
        if (number != null) {
            val txtTop = text(number.toString())
            val txtBottom = text(number.toString())
            txtTop.alignTopToTopOf(this, padding = 8)
            txtTop.alignLeftToLeftOf(this, padding = 14)
            txtBottom.alignBottomToBottomOf(this, padding = 8)
            txtBottom.alignRightToRightOf(this, padding = 11)
            addChild(txtBottom)
            addChild(txtTop)
        }
        addChild(img)
    }
}

class HandContainer(
    private val cardImages: Map<String, Bitmap>
) : Container() {
    fun update(hand: MutableList<Card>){
        var padding = 0
        for (card in hand){
            val cardContainer = CardContainer(card.number, cardImages[card.color]!!)
            cardContainer.xy(padding, 0)
            addChild(cardContainer)
            padding += 30
        }
    }
}
