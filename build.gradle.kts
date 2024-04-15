buildscript {
//    repositories {
//        maven {
//            url = uri("https://maven.pkg.github.com/google/secrets-gradle-plugin")
//            credentials {
//                username = project.findProperty("GITHUB_USER") ?: System.getenv("nlin004")
//                password = project.findProperty("GITHUB_TOKEN") ?: System.getenv("GITHUB_TOKEN")
//            }
//        }
//    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    // Add the dependency for the Google services Gradle plugin

    id("com.google.gms.google-services") version "4.4.1" apply false
}