plugins {
    id("com.android.test")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.benchmark"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        targetSdk = 36

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Suppress Macrobenchmark debuggable warning
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "DEBUGGABLE"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildTypes {
        debug {
            // Benchmark module is test-only, debug is sufficient
            isDebuggable = true
        }
    }

    // Specify the target app module to test
    targetProjectPath = ":app"

    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    // Macrobenchmark JUnit4
    implementation("androidx.benchmark:benchmark-macro-junit4:1.2.4")

    // AndroidX Test core libraries
    implementation("androidx.test:runner:1.7.0")
    implementation("androidx.test:rules:1.7.0")

    // UI Automator for scrolling, clicking, typing
    implementation("androidx.test.uiautomator:uiautomator:2.3.0")

    implementation("com.google.android.material:material:1.9.0")
    implementation(libs.androidx.junit.ktx)
}
