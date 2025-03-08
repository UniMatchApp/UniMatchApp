import com.android.build.api.dsl.Optimization

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"

    // Google services Gradle plugin
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0" // o la versión que uses

}


android {
    namespace = "com.ulpgc.uniMatch"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ulpgc.uniMatch"
        minSdk = 33
        targetSdk = 34
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
        }
    }
}

dependencies {
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.firebase.messaging.ktx)
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
    implementation("dev.shreyaspatil.permission-flow:permission-flow-android:2.0.0")

    // Places Api
    implementation("com.google.android.libraries.places:places:3.3.0")

    // Lifecycle
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0-alpha03")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0-alpha03")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-alpha03")

    // Logger
    implementation("io.github.aakira:napier:2.6.1")

    // Ktor
    implementation("io.ktor:ktor-client-okhttp:2.2.1")
    implementation("io.ktor:ktor-client-core:2.2.1")
    implementation("io.ktor:ktor-client-logging:2.2.1")
    implementation("io.ktor:ktor-client-serialization:2.2.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.1")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    implementation("io.github.cdimascio:dotenv-kotlin:6.5.0")

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

    implementation("androidx.core:core-splashscreen:1.0.1")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")

}

tasks.register("reverseDevicePorts") {
    doLast {
        exec {
            commandLine("powershell", "-NoProfile", "-ExecutionPolicy", "Bypass", "-File", "../reverse_device_ports.ps1")
        }
    }
}

tasks.whenTaskAdded {
    if (name == "compileDebugKotlin") {
        dependsOn("reverseDevicePorts")
    }
}