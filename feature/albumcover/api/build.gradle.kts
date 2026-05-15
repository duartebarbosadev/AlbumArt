plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.sample.feature.albumcover.api"
}
dependencies {
    implementation(libs.androidx.navigation3.runtime)
}
