import korlibs.image.bitmap.*
import korlibs.image.format.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.io.file.std.resourcesVfs


class DeskScene : Scene() {
    override suspend fun SContainer.sceneMain() {
        val bitmapBack = resourcesVfs["cardBack.png"].readBitmap()
        val bitmapBlue = resourcesVfs["cardBGb.png"].readBitmap()
        val bitmapGreen = resourcesVfs["cardBGg.png"].readBitmap()
        val bitmapPink = resourcesVfs["cardBGp.png"].readBitmap()
        val bitmapYellow = resourcesVfs["cardBGy.png"].readBitmap()

        val deskTop = CardContainer(2, bitmapGreen)
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
