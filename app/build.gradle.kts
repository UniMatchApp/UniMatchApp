import com.android.build.api.dsl.Optimization

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
//    id("org.jetbrains.kotlin.android") version "1.9.20"

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



//tasks.whenTaskAdded {
//    if (name == "compileDebugKotlin") {
//        dependsOn("generateDebugResources")
//        println("Running adb reverse using PowerShell script...")
//
//        doFirst {
//            exec {
//                commandLine("powershell", "-NoProfile", "-ExecutionPolicy", "Bypass", "-File", "../reverse_device_ports.ps1")
//            }
//        }
//    }
//}

//gradle.taskGraph.whenReady {
//    allTasks.forEach { task ->
//        val adbCommand = "adb reverse tcp:3000 tcp:3000"
//        val adbCommand2 = "adb reverse tcp:8080 tcp:8080"
//        val adbCommand3 = "adb reverse tcp:8081 tcp:8081"
//        task.doFirst {
//            println("Running adb reverse...")
//            exec {
//                commandLine("cmd", "/c", adbCommand)
//            }
//            exec {
//                commandLine("cmd", "/c", adbCommand2)
//            }
//            exec {
//                commandLine("cmd", "/c", adbCommand3)
//            }
//        }
//    }
//}