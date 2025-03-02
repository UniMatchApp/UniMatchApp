import com.android.build.api.dsl.Optimization
import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"

    // Google services Gradle plugin
    id("com.google.gms.google-services")

    // Dagger Hilt
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")

}

android {
    namespace = "com.ulpgc.uniMatch"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ulpgc.uniMatch"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        javaCompileOptions {
            annotationProcessorOptions {
                arguments.putAll(mapOf("room.schemaLocation" to "$projectDir/schemas"))
            }
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    buildTypes {
        debug {
            isDebuggable = true
            enableAndroidTestCoverage = false
            isMinifyEnabled = false

            buildConfigField("String", "BASE_URL", "\"http://localhost:3000/\"")
            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
            )
            kotlinOptions {
                freeCompilerArgs = listOf("-Xdebug")
            }
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"https://prod-api-url.com:3000/\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.foundation.layout.android)
    ksp(libs.androidx.room.compiler.v261)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.room.common)
    implementation(libs.volley)
    implementation(libs.protolite.well.known.types)
    implementation(libs.coil.compose)
    implementation(libs.accompanist.flowlayout)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Draggable grid
    implementation(libs.reorderable)


    implementation(libs.ui)
    implementation(libs.google.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.ktx.v230)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.play.services.location.v2101)

    // Permissions
    implementation(libs.permission.flow.android.v200)

    // ImagePicker
    implementation(libs.imagepicker)

    // Retrofit 2
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Extended Icons
    implementation(libs.androidx.material.icons.extended)

    // Coil
    implementation(libs.coil.compose)

    // Secure Storage
    implementation(libs.androidx.security.crypto)

    // Coroutines
    implementation(libs.kotlinx.coroutines.play.services)

    // encryption
    implementation(libs.bcrypt)

    // OkHttp
    implementation(libs.okhttp)

    implementation(libs.androidx.core.splashscreen)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // Dagger Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)


}

kapt {
    correctErrorTypes = true
}

tasks.register("reverseDevicePorts") {
    doLast {
        exec {
            commandLine("adb", "reverse", "tcp:8081", "tcp:8081")
        }
    }
}

tasks.whenTaskAdded {
    if (name == "compileDebugKotlin") {
        dependsOn("reverseDevicePorts")
    }
}