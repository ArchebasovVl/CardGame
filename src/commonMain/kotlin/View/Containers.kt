package View

import Card
import korlibs.image.bitmap.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*

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

class BotHandContainer(
    private val cardImages: Map<String, Bitmap>
) : Container() {

    init { text(" ") }

    fun update(hand: Int) {
        for (cardNumber in 1..hand) {
            val cardContainer = CardContainer(null, cardImages)
            cardContainer.alignLeftToLeftOf(this, 30 * cardNumber)
            cardContainer.alignTopToTopOf(this)
            addChild(cardContainer)
        }
    }
}
