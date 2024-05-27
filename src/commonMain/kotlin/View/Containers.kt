package View

import Card
import ViewModel
import korlibs.image.bitmap.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*

open class CardContainer(
    val card: Card?,
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
        if (card != null) {
            changeCard(card)
        }
    }

    fun changeCard(card: Card) {
        when (card.number){
            10 -> {
                img = image(cardImages[card.color + "Hp"]!!) { scale = 0.04 }
                txtTop.setText("")
                txtBottom.setText("")
            }
            11 -> {
                img = image(cardImages[card.color + "St"]!!) { scale = 0.04 }
                txtTop.setText("")
                txtBottom.setText("")
            }
            else -> {
                img = image(cardImages[card.color]!!) { scale = 0.04 }
                txtTop.setText(card.number.toString())
                txtBottom.setText(card.number.toString())
            }
        }
        addChild(txtTop)
        addChild(txtBottom)
    }
}

class MovableCardContainer(
    card: Card?,
    cardImages: Map<String, Bitmap>,
    parent: HandContainer,
    index: Int
) : CardContainer(card, cardImages) {
    init{
        onDown {
            parent.vm.putCard(index)
        }
    }
}


class HandContainer(
    private val cardImages: Map<String, Bitmap>,
    val vm: ViewModel
) : Container() {

    init { text(" ") }

    fun update(hand: MutableList<Card>) {
        while (this.children.size > 1) this.removeChildAt(this.children.size - 1)
        for ((index, card) in hand.withIndex()) {
            val cardContainer = MovableCardContainer(card, cardImages, this, index)
            cardContainer.alignLeftToLeftOf(this, index * 30)
            cardContainer.alignBottomToBottomOf(this)
            addChild(cardContainer)
        }
        removeChildAt(0)
    }
}

class BotHandContainer(
    private val cardImages: Map<String, Bitmap>
) : Container() {

    init { text(" ") }

    fun update(hand: Int) {
        while (this.children.size > 1) this.removeChildAt(this.children.size - 1)
        for (cardNumber in 1..hand) {
            val cardContainer = CardContainer(null, cardImages)
            cardContainer.alignLeftToLeftOf(this, 30 * cardNumber)
            cardContainer.alignTopToTopOf(this)
            addChild(cardContainer)
        }
        removeChildAt(0)
    }
}
