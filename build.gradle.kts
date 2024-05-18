import korlibs.korge.gradle.*

plugins {
	alias(libs.plugins.korge)
}

korge {
	targetJvm()
	targetJs()
	targetIos()
	targetAndroid()

	serializationJson()
}


dependencies {
    add("commonMainApi", project(":deps"))
}

