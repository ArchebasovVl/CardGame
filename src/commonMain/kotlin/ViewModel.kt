import kotlinx.coroutines.flow.*

class ViewModel {
    private val gameProcess = Game()
    private val _deskTopFlow = MutableSharedFlow<Card>()
    private val _handFlow = MutableSharedFlow<MutableList<Card>>()
    private val _botHandFlow = MutableSharedFlow<Int>()
    val deskTopFlow: SharedFlow<Card> get() = _deskTopFlow
    val handFlow: SharedFlow<MutableList<Card>> get() = _handFlow
    val botHandFlow: SharedFlow<Int> get() = _botHandFlow

    suspend fun emitAll(deskTopCard: Card, handCards: MutableList<Card>, botCardCount: Int) {
        _deskTopFlow.emit(deskTopCard)
        _handFlow.emit(handCards)
        _botHandFlow.emit(botCardCount)
    }

    fun putCard(index: Int){
        gameProcess.round(index)
    }

    fun skipRound(){
        gameProcess.round(-1)
    }

    fun getCard(){
        gameProcess.playertakes(1)
        gameProcess.round(-2)
    }
}
