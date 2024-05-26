import View.*
import korlibs.image.color.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.math.geom.*

suspend fun main() = Korge(
    windowSize = Size(512, 512),
    backgroundColor = Colors["#2b2b2b"])
{
	val sceneContainer = sceneContainer()
    val deskScene = DeskScene()
	sceneContainer.changeTo { deskScene }
}

