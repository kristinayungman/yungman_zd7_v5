pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // ❌ НЕТ maven("https://api.mapbox.com/...")
    }
}

rootProject.name = "zd7_v5_yungman"
include(":app")