plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.8.0"
    id("com.android.library")
    id("com.google.gms.google-services")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation ("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                // COROUTINES
                //implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.6.4")

                // MOKO - MVVM
                //implementation ("dev.icerock.moko:mvvm:0.15.0")

                // SERIALIZATION
                //implementation ("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:1.5.0-RC")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0-RC")

                // KODE IN
                implementation ("io.insert-koin:koin-core:3.2.2")
                implementation("io.insert-koin:koin-test:3.2.2")

                // FIRE STORE
                //implementation ("co.touchlab:firestore:0.2.5")
                //implementation("dev.gitlive:firebase-auth:1.6.2")
                //implementation("dev.gitlive:firebase-database:1.6.2")
                //implementation("dev.gitlive:firebase-firestore:1.6.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                //implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
                //implementation ("org.jetbrains.kotlin:kotlin-stdlib-common:1.8.10")

                // COROUTINES
                //implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

                // MOKO - MVVM
                //implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

                // SERIALIZATION
                //implementation ("org.jetbrains.kotlinx:kotlinx-serialization-runtime:1.5.0-RC")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                //
                implementation("androidx.paging:paging-common-ktx:3.1.1")

                // FIREBASE
                //implementation ("com.google.firebase:firebase-core:21.1.1")
                //implementation ("com.google.firebase:firebase-firestore:24.4.3")
                implementation("com.google.firebase:firebase-auth-ktx:21.1.0")
                implementation("com.google.firebase:firebase-database-ktx:20.1.0")
                implementation("com.google.firebase:firebase-storage-ktx:20.1.0")
                implementation("com.google.firebase:firebase-firestore-ktx:24.4.4")
                implementation("com.github.bumptech.glide:glide:4.14.2")
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                //implementation ("org.jetbrains.kotlin:kotlin-stdlib-common:1.8.10")

                // COROUTINE
                //implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.8")

                // SERIALIZATION
                //implementation ("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:1.5.0-RC")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.example.rfeventapp"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
}
