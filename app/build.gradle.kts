import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.dagger.hilt)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.kimothorick.plashr"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kimothorick.plashr"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["appAuthRedirectScheme"] = "plashr://callback"

        val apikeyPropertiesFile = project.rootProject.file("apikey.properties")
        val apikeyProperties = Properties()
        apikeyProperties.load(apikeyPropertiesFile.inputStream())

        val client_id = apikeyProperties.getProperty("client_id")
        val client_secret = apikeyProperties.getProperty("client_secret")
        val redirect_uri = apikeyProperties.getProperty("callback_url")

        buildConfigField(
            type = "String", name = "client_id", value = client_id
        )
        buildConfigField(
            type = "String", name = "client_secret", value = client_secret
        )
        buildConfigField(
            type = "String", name = "redirect_uri", value = redirect_uri
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.navigation.runtime)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /*Navigation suite*/
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.material3.adaptive)
    implementation(libs.androidx.material.icons.extended)
    /*Material 3*/
    implementation(libs.androidx.material3)
    /*Cascade*/
    implementation(libs.cascade.compose)
    /*Serializable*/
    implementation(libs.kotlinx.serialization.json)
    /*Adaptive*/
    implementation(libs.androidx.adaptive.layout)
    /*Hilt navigation fragment*/
    implementation(libs.androidx.hilt.navigation.fragment)
    /*DataStore*/
    implementation(libs.androidx.datastore.preferences)
    /*Hilt*/
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    /*Retrofit */
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.kotlin.coroutine.adapter)
    /*Coil*/
    implementation(libs.coil.compose)
    /*App Auth*/
    implementation(libs.appauth)
}
