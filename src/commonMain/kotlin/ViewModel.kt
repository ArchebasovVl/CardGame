import kotlinx.coroutines.flow.*

class ViewModel {
    private val gameProcess = Game()
    private val _deskTopFlow = MutableSharedFlow<Card>()
    private val _handFlow = MutableSharedFlow<MutableList<Card>>()
    private val _botHandFlow = MutableSharedFlow<Int>()
    val deskTopFlow: SharedFlow<Card> get() = _deskTopFlow
    val handFlow: SharedFlow<MutableList<Card>> get() = _handFlow
    val botHandFlow: SharedFlow<Int> get() = _botHandFlow

    private suspend fun emitAll(deskTopCard: Card, handCards: MutableList<Card>, botCardCount: Int) {
        _deskTopFlow.emit(deskTopCard)
        _handFlow.emit(handCards)
        _botHandFlow.emit(botCardCount)
    }

    suspend fun putCard(index: Int) {
        val response: Triple<Card, MutableList<Card>, Int> = gameProcess.round(index)
        emitAll(response.first, response.second, response.third)
    }

    suspend fun skipRound() {
        val response: Triple<Card, MutableList<Card>, Int> = gameProcess.round(-1)
        emitAll(response.first, response.second, response.third)
    }

    suspend fun getCard() {
        val response2: Triple<Card, MutableList<Card>, Int> = gameProcess.round(-2)
        emitAll(response2.first, response2.second, response2.third)
    }
}
