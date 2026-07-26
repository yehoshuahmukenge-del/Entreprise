plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "cd.esforca.entreprise"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "cd.esforca.entreprise"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Interface Utilisateur (UI)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.recyclerview)

    // Architecture MVVM
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)

    // Persistance (Room)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

    // Images & UX
    implementation(libs.glide)
    implementation(libs.androidx.core.splashscreen)
}