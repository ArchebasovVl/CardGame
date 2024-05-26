import korlibs.image.color.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import kotlinx.coroutines.*

suspend fun main() = Korge(
    windowSize = Size(512, 512),
    backgroundColor = Colors["#2b2b2b"])
{
    val viewModel = ViewModel()
	val sceneContainer = sceneContainer()
    val deskScene = DeskScene()
	sceneContainer.changeTo { deskScene }

    launch {
        viewModel.handFlow.collect { hand ->
            deskScene.hand.update(hand)
            deskScene.hand.centerXOnStage()
        }
    }
}

