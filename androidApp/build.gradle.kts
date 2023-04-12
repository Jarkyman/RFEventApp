plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.rfeventapp.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.example.rfeventapp.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    //Compose
    implementation("androidx.compose.ui:ui:1.3.3")
    implementation("androidx.compose.ui:ui-tooling:1.3.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.3")
    implementation("androidx.compose.foundation:foundation:1.3.1")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.compose.material:material-icons-core:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.3.1")


    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.navigation:navigation-compose:2.5.3")

    implementation("androidx.constraintlayout:constraintlayout:2.2.0-alpha07")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("io.coil-kt:coil-compose:1.3.2")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:31.2.2"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    //Datetime
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")
    implementation("io.github.grizzi91:bouquet:1.0.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.2")

    //KOIN
    implementation ("io.insert-koin:koin-core:3.2.2")
    implementation("io.insert-koin:koin-androidx-compose:3.3.0")
    implementation("io.insert-koin:koin-android:3.3.0")

    //PDF Viewer
    implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")

}