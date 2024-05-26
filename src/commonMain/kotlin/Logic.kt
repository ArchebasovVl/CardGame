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

}

class Player (stack: Stack, desk: Desk) {
    val hand: Hand = Hand(stack, desk)

    fun check (inx: Int, desk: Desk, botSkipped: Boolean, resCard: Boolean): Boolean {
        if (inx == -1) return true
        if (inx == -2) return !resCard
        if (desk.getCard().number == 10 && !(botSkipped)) if (hand.cards[inx].number != 10) return false
        if (desk.getCard().number == 11 && !(botSkipped)) if (hand.cards[inx].number != 11) return false
        return desk.getCard().checkRight(hand.cards[inx])
    }

    fun move (inx: Int, desk: Desk) {
        if (hand.cards[inx].number != -1) hand.makeMove(inx, desk)
    }

    fun take (cnt: Int, stack: Stack, desk: Desk) {
        for (i in 0..cnt) hand.takeCard(stack, desk)
    }
}

class Bot (stack: Stack, desk: Desk) {
    val hand: Hand = Hand(stack, desk)
    private fun makeReaction (stack: Stack, desk: Desk): Boolean {
        if (desk.getCard().number == 10) {
            if (hand.cards.count {it.number == 10} == 0)
                hand.takeCard(stack, desk)
                hand.takeCard(stack, desk)
                hand.takeCard(stack, desk)
                return false
        }
        else if (desk.getCard().number == 11) {
            if (hand.cards.count {it.number == 10} == 0) return false
        }
        return true
    }
    private fun makeMove(desk: Desk): Pair<Card, Int> {
        if (desk.getCard().number == 10) {
            for (color in  listOf("Blue", "Green", "Pink", "Yellow")) {
                if (hand.cards.indexOf(Card(color, 10)) != -1) {
                    hand.makeMove(hand.cards.indexOf(Card(color, 10)), desk)
                    return Pair(Card(color, 10), 1)
                }
            }

        }
        else if (desk.getCard().number == 11) {
            for (color in  listOf("Blue", "Green", "Pink", "Yellow")) {
                if (hand.cards.indexOf(Card(color, 11)) != -1) {
                    hand.makeMove(hand.cards.indexOf(Card(color, 11)), desk)
                    return Pair(Card(color, 10), 1)
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
                        if (hand.cards.count { it == card } == 2) {
                            hand.makeMove(hand.cards.indexOf(card), desk)
                            return Pair(card, 2)
                        }
                        return Pair(card, 1)
                    }

                }
            }
        }
        return Pair(Card("", -1), 0)
    }
    fun makeRound(stack: Stack, desk: Desk): Pair<Card, Int> {
        if (makeReaction(stack, desk)) {
            if (makeMove(desk) == Pair(Card("", -1), 0)) {
                hand.takeCard(stack, desk)
                return makeMove(desk)
            }
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
    private lateinit var botMove: Pair<Card, Int>

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

    fun round(plrmove: Int): Triple<Card, MutableList<Card>, Int> {
        if (player.check(plrmove, desk, botMove.second == 0, plrTakedCard)) {
            if (plrmove == -1)  {
                plrTakedCard = false
                if (botMove.second != 0)  {
                    if (desk.getCard().number == 10) player.take(3, stack, desk)
                }
            }
            else if (plrmove != -2) {
                plrTakedCard = false
                player.move(plrmove, desk)
                botMove = bot.makeRound(stack, desk)

            }
            else {
                plrTakedCard = true
            }
        }
        return Triple(desk.getCard(), player.hand.cards, bot.hand.cards.size)
    }
}
