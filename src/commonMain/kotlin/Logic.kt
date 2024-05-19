import kotlin.random.Random

// класс Desk который хранит карты которые на столе а так же проверяет полученные карты

class Desk () {
    private val cards: MutableList<Int> = mutableListOf()

    fun getDeskstack

}

// класс Stack который хранит колоду возвращает последнюю карту берущему и забирает карты со стола
class Stack (){
    private val cards: MutableList<Int> = mutableListOf()
    init {
        for (color in  1..4) {
            for (number in 0..9) {
                cards.add(color*10 + number)
            }
        }
        cards.shuffle()
    }

    fun getCard(desk: Desk): Int {
        if (cards.size!= 0) {
            val getting = cards[cards.size - 1]
            cards.removeLast()
            return getting
        }
        else {
            cards = desk.getDeskstack().shuffle() // <- взять карты выброшенные на стол и перемешать
            val getting = cards[cards.size - 1]
            cards.removeLast()
            return getting
        }
    }
}

class Hand () {
    private val cards: MutableList<Int> = mutableListOf()
    fun initial(stack: Stack) { //я просто хз как это через init прописать
        for (count in 0..8) {
            cards.add(stack.getCard()) //Берет карту с колоды
        }
    }

    fun takeCard(stack: Stack) {
        cards.add(stack.getCard())
    }

    fun makeMove()


}