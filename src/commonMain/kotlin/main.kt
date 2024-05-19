import korlibs.image.color.*
import korlibs.image.format.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*

suspend fun main() = Korge(
    windowSize = Size(512, 512),
    backgroundColor = Colors["#2b2b2b"])
{
    val viewModel = ViewModel()
	val sceneContainer = sceneContainer()
	sceneContainer.changeTo { DeskScene() }
}

