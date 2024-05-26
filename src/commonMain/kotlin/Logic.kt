//import kotlin.random.Random
//
class Card (color: String, number: Int) {
    val color: String = color
    val number: Int = number
}
//
//// класс Desk который хранит карты которые на столе а так же проверяет полученные карты
//
//class Desk () {
//    private val cards: MutableList<Int> = mutableListOf()
//
//    fun getDeskstack () {
//        return cards
//    }
//
//    fun getCard () {
//        return cards[cards.size - 1]
//    }
//
//    fun addCard (card: Int) {
//
//    }
//
//}
//
//// класс Stack который хранит колоду возвращает последнюю карту берущему и забирает карты со стола
//class Stack (){
//    private val cards: MutableList<Int> = mutableListOf()
//    init {
//        for (color in  1..4) {
//            for (number in 0..9) {
//                cards.add(color*10 + number)
//            }
//        }
//        cards.shuffle()
//    }
//
//    fun getCard(desk: Desk): Int {
//        if (cards.size!= 0) {
//            return cards.removeLast()
//        }
//        else {
//            cards.clear()
//            cards.addAll(0, desk.getDeskstack().shuffle()) // <- взять карты выброшенные на стол и перемешать
//            return cards.removeLast()
//        }
//    }
//}
//
//class Hand (stack: Stack, desk: Desk) {
//    private val cards: MutableList<Int> = mutableListOf()
//    init { //я просто хз как это через init прописать
//        for (count in 0..8) {
//            cards.add(stack.getCard(desk)) //Берет карту с колоды
//        }
//    }
//
//    fun takeCard(stack: Stack) {
//        cards.add(stack.getCard())
//    }
//
//    fun makeMove()
//
//
//}
