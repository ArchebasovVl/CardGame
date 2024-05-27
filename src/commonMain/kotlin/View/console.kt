package View

import Card
import Game

fun main_c () {
    var inp = ""
    val game = Game()
    var outp = ""
    var res: Triple<Card, MutableList<Card>, Int> = game.start()
    while (res.third != 100 && res.third != -100) {
        println("Раунд:/n/tСтол: " + res.first.number.toString() + " " + res.first.color + "/n/tРука бота: " + res.third.toString() + "/n/tРука игрока:")
        outp = ""
        for (i in 0..(res.second.size-1)) {
            outp += "[" + res.second[i].number.toString() + " " +res.second[i].color+ "] "
        }
        println(outp)
        inp = readln()
        when (inp) {
            "Take" -> res = game.round(-2)
            "Skip" -> res = game.round(-1)
            else -> res = game.round(inp.toInt())
        }
    }
    if (res.third == 100) println("Победа бота")
    else if (res.third == -100) println("Победа игрока")
}
