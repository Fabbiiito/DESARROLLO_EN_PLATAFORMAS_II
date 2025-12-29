pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        // 🔑 CORRECCIÓN: Se ELIMINÓ 'org.jetbrains.kotlin.plugin.compose'

        // Mantener KSP y Serialización (si se usan)
        id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
        id("com.google.devtools.ksp") version "2.0.21-1.0.27"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NutriaxDBP"
include(":app")
 