// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.spotless) apply false
}


subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude(
                "${layout.buildDirectory}/**/*.kt",
            )
            ktlint()
        }

        kotlinGradle {
            target("*.gradle.kts")
            targetExclude(
                "${layout.buildDirectory}/**/*.kt",
                "build.gradle.kts"
            )
            ktlint()
        }

        format("xml") {
            target("**/*.xml")
            targetExclude(
                "${layout.buildDirectory}/**/*.xml",
                "**/build-reports/**/*.xml",
                "src/main/res/drawable/ic_launcher_background.xml"
            )
        }
    }
}
