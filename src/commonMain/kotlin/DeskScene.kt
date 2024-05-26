import korlibs.image.bitmap.*
import korlibs.image.format.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.io.file.std.resourcesVfs


class DeskScene : Scene() {
    override suspend fun SContainer.sceneMain() {
        val colorToBitmap = mapOf(
            "back" to resourcesVfs["cardBack.png"].readBitmap(),
            "blue" to resourcesVfs["cardBGb.png"].readBitmap(),
            "green" to resourcesVfs["cardBGg.png"].readBitmap(),
            "pink" to resourcesVfs["cardBGp.png"].readBitmap(),
            "yellow" to resourcesVfs["cardBGy.png"].readBitmap()
            )

        val deskTop = CardContainer(2, colorToBitmap["back"]!!)
        deskTop.centerOn(this)
        addChild(deskTop)
    }
}

class CardContainer(number: Int, cardBitmap: Bitmap) : Container() {
    init {
        val img = image(cardBitmap) { scale = 0.04 }
        img.centerOn(this)
        val txtTop = text(number.toString())
        val txtBottom = text(number.toString())
        txtTop.alignTopToTopOf(this, padding = 8)
        txtTop.alignLeftToLeftOf(this, padding = 14)
        txtBottom.alignBottomToBottomOf(this, padding = 8)
        txtBottom.alignRightToRightOf(this, padding = 11)
        addChild(img)
        addChild(txtBottom)
        addChild(txtTop)
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
