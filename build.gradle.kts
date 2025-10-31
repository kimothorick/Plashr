plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.cash.paparazzi).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.kotlin.parcelize).apply(false)
    alias(libs.plugins.detekt).apply(true)
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false

    kotlin("jvm")
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
    google()
}
kotlin {
    jvmToolchain(8)
}
