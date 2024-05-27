class Card (val color: String, val number: Int) {

    fun checkRight (card: Card): Boolean {
        if (card.color == color) return true
        else if (card.number == number) return true
        else if (card.number == 12) return true
        return false
    }
}

// класс Desk который хранит карты которые на столе а так же проверяет полученные карты
class Desk {
    private val cards: MutableList<Card> = mutableListOf()

    fun getDeskStack (): MutableList<Card> {
        val stk: MutableList<Card> = cards.subList(0, cards.size)
        cards.subtract(stk)
        return stk
    }

    fun getCard (): Card {
        return cards[cards.size - 1]
    }

    fun addCard (card: Card) {
        cards.add(card)
    }

}

// класс Stack который хранит колоду возвращает последнюю карту берущему и забирает карты со стола
class Stack {
    private val cards: MutableList<Card> = mutableListOf()
    init {
        for (color in  listOf("Blue", "Green", "Pink", "Yellow")) {
            for (number in 0..11) {
                if (number != 12) {
                    cards.add(Card(color, number))
                    cards.add(Card(color, number))
                }
                else cards.add(Card("Black", number))
            }
        }
        cards.shuffle()
    }

    fun getCard(desk: Desk): Card {
        if (cards.size != 0) {
            return cards.removeLast()
        }
        else {
            cards.clear()
            cards.addAll(0, desk.getDeskStack().toMutableList())
            cards.shuffle() // <- взять карты выброшенные на стол и перемешать
            return cards.removeLast()
        }
    }
}

class Hand (stack: Stack, desk: Desk) {
    val cards: MutableList<Card> = mutableListOf()
    init {
        for (count in 0..8) {
            cards.add(stack.getCard(desk)) //Берет карту с колоды
        }
    }

    fun takeCard(stack: Stack, desk: Desk) {
        cards.add(stack.getCard(desk))
    }

    fun makeMove(inx: Int, desk: Desk) {
        desk.addCard(cards.removeAt(inx))
    }

    fun ifin(card: Card): Int {
        for (ind in 0..(cards.size - 1)) if (cards[ind].color == card.color && cards[ind].number == card.number) return ind
        return -1
    }

}

class Player (stack: Stack, desk: Desk) {
    val hand: Hand = Hand(stack, desk)

    fun check (inx: Int, desk: Desk, botSkipped: Boolean, resCard: Boolean): Boolean {
        if (inx == -1 && desk.getCard().number == 10) return true
        if (inx == -1 && desk.getCard().number == 11) return true
        if (inx == -1) return resCard
        if (inx == -2 && desk.getCard().number == 10) return false
        if (inx == -2 && desk.getCard().number == 11 && botSkipped) return false
        if (inx == -2 && desk.getCard().number == 11 && !botSkipped) return true
        if (inx == -2 && desk.getCard().number == 10 && botSkipped) return false
        if (inx == -2 && desk.getCard().number == 10 && !botSkipped) return true
        if (inx == -2 && desk.getCard().number != 10) return !resCard
        println(desk.getCard().number.toString() + " " + hand.cards[inx].number.toString())
        if (desk.getCard().number == 10 && !(botSkipped)) if (hand.cards[inx].number != 10) return false
        if (desk.getCard().number == 11 && !(botSkipped)) if (hand.cards[inx].number != 11) return false
        return desk.getCard().checkRight(hand.cards[inx])
    }

    fun move (inx: Int, desk: Desk) {
        if (hand.cards[inx].number != -1) hand.makeMove(inx, desk)
    }

    fun take (cnt: Int, stack: Stack, desk: Desk) {
        for (i in 1..cnt) hand.takeCard(stack, desk)
    }
}

class Bot (stack: Stack, desk: Desk) {
    val hand: Hand = Hand(stack, desk)
    private fun makeReaction (stack: Stack, desk: Desk, plrskip: Boolean = false): Boolean {
        if (desk.getCard().number == 10 && !plrskip) {
            if (hand.ifin(Card("Pink", 10)) == -1 && hand.ifin(Card("Yellow", 10))  == -1 && hand.ifin(Card("Green", 10))  == -1  && hand.ifin(Card("Blue", 10))  == -1 )
                hand.takeCard(stack, desk)
            hand.takeCard(stack, desk)
            hand.takeCard(stack, desk)
            return false
        }
        else if (desk.getCard().number == 11 && !plrskip) {
            if (hand.ifin(Card("Pink", 11))  == -1 && hand.ifin(Card("Yellow", 11))  == -1 && hand.ifin(Card("Green", 11))  == -1  && hand.ifin(Card("Blue", 11))  == -1) return false
        }
        return true
    }
    private fun makeMove(desk: Desk, plrskip: Boolean = false): Pair<Card, Int> {
        if (desk.getCard().number == 10 && !plrskip) {
            for (color in  listOf("Blue", "Green", "Pink", "Yellow")) {
                if (hand.cards.indexOf(Card(color, 10)) != -1) {
                    hand.makeMove(hand.ifin(Card(color, 10)), desk)
                    return Pair(Card(color, 10), 1)
                }
            }

        }
        else if (desk.getCard().number == 11 && !plrskip) {
            for (color in  listOf("Blue", "Green", "Pink", "Yellow")) {

                if (hand.ifin(Card(color, 11)) != -1)  {
                    hand.makeMove(hand.ifin(Card(color, 11)), desk)
                    return Pair(Card(color, 11), 1)
                }
            }
        }
        else {
            for (card in hand.cards) {
                if (desk.getCard().checkRight(card)) {
                    if (card == Card("Black", 12)) {
                        val inx = hand.cards.indexOf(card)
                        val colorCounts = hand.cards.groupBy { it.color }.mapValues { it.value.size }
                        val mostFrequentColor = colorCounts.maxByOrNull { it.value }?.key
                        card.color.drop(card.color.length).plus(mostFrequentColor)//очистить переменную и вставить новое значение
                        hand.makeMove(inx, desk)
                        return Pair(Card(mostFrequentColor+"", 12), 1)
                    }
                    else {
                        hand.makeMove(hand.cards.indexOf(card), desk)
                        //if (hand.cards.count { it == card } == 2) {
                        //    hand.makeMove(hand.cards.indexOf(card), desk)
                        //    return Pair(card, 2)
                        //}
                        return Pair(card, 1)
                    }

                }
            }
        }
        return Pair(Card("", -1), 0)
    }
    fun makeRound(stack: Stack, desk: Desk, plrskip: Boolean = false): Pair<Card, Int> {
        var outp = "Руки бота: "
        for (i in 0..(hand.cards.size-1)) {
            outp += "[" + hand.cards[i].number.toString() + " " +hand.cards[i].color+ "] "
        }
        println(outp)
        val react: Boolean = makeReaction(stack, desk, plrskip)
        println(react)
        var mamo: Pair<Card, Int>
        if (react) {
            mamo = makeMove(desk, plrskip)

            if (mamo.first.number == -1 ) {
                hand.takeCard(stack, desk)
                mamo = makeMove(desk, plrskip)
            }
            return mamo
        }
        return Pair(Card("", -1), 0)
    }
}


class Game {
    private val stack: Stack = Stack()
    private val desk: Desk = Desk()
    private val player: Player
    private val bot: Bot
    private var plrTakedCard: Boolean = false
    private var botMove: Pair<Card, Int> = Pair(Card("Black", 11), 0)
    init {
        desk.addCard(stack.getCard(desk))
        while (desk.getCard().number > 9) desk.addCard(stack.getCard(desk))
        bot = Bot(stack, desk)
        player = Player(stack, desk)
    }

    fun playertakes(cnt: Int): MutableList<Card> {
        player.take(cnt, stack, desk)
        return player.hand.cards
    }

    fun start(): Triple<Card, MutableList<Card>, Int>  {
        return Triple(desk.getCard(), player.hand.cards, bot.hand.cards.size)
    }

    fun round(plrmove: Int): Triple<Card, MutableList<Card>, Int> {
        if (player.check(plrmove, desk, botMove.second == 0, plrTakedCard)) {
            if (plrmove == -1)  {
                plrTakedCard = false
                println(botMove.first.color)
                println(botMove.first.number)
                if (botMove.second != 0)  {
                    if (desk.getCard().number == 10) playertakes(3)
                }
                botMove = bot.makeRound(stack, desk, true)
                if (bot.hand.cards.size == 0) return Triple(desk.getCard(), player.hand.cards, 100) // Проигрыш игрока
            }
            else if (plrmove != -2) {
                plrTakedCard = false
                player.move(plrmove, desk)
                if (player.hand.cards.size == 0) return Triple(desk.getCard(), player.hand.cards, -100) // Проигрыш бота
                botMove = bot.makeRound(stack, desk)
                if (bot.hand.cards.size == 0) return Triple(desk.getCard(), player.hand.cards, 100) // Проигрыш игрока

            }
            else {
                playertakes(1)
                plrTakedCard = true
            }
        }
        else println("Неверный ход")
        return Triple(desk.getCard(), player.hand.cards, bot.hand.cards.size)
    }
}
