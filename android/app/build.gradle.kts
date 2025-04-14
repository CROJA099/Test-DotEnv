plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

import java.util.Properties
import java.io.FileInputStream

val keystoreProperties = Properties()
val keystorePropertiesFile = rootProject.file("key.properties")
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.example.test_dotenv"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
   

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.example.test_dotenv"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    

    signingConfigs {
        create("mazdaConfig") {
            if (System.getenv("CI") != null) {
                storeFile = file(System.getenv("MAZDA_KEYSTORE_PATH"))
                storePassword = System.getenv("MAZDA_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("MAZDA_KEY_ALIAS")
                keyPassword = System.getenv("MAZDA_KEY_PASSWORD")
            } else {
                storeFile = keystoreProperties["mazda_storeFile"]?.let { file(it as String) }
                storePassword = keystoreProperties["mazda_storePassword"] as String?
                keyAlias = keystoreProperties["mazda_keyAlias"] as String?
                keyPassword = keystoreProperties["mazda_keyPassword"] as String?
            }
        }

        create("nissanConfig") {
            if (System.getenv("CI") != null) {
                storeFile = file(System.getenv("NISSAN_KEYSTORE_PATH"))
                storePassword = System.getenv("NISSAN_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("NISSAN_KEY_ALIAS")
                keyPassword = System.getenv("NISSAN_KEY_PASSWORD")
            } else {
                storeFile = keystoreProperties["nissan_storeFile"]?.let { file(it as String) }
                storePassword = keystoreProperties["nissan_storePassword"] as String?
                keyAlias = keystoreProperties["nissan_keyAlias"] as String?
                keyPassword = keystoreProperties["nissan_keyPassword"] as String?
            }
        }
    }

    flavorDimensions("flavor-type")
    productFlavors {
        create("mazda") {
            dimension = "flavor-type"
            applicationId = "com.example.mazda"
            resValue(type = "string", name = "app_name", value = "Mazda App")
            signingConfig = signingConfigs.getByName("mazdaConfig")            
        }
        create("nissan") {
            dimension = "flavor-type"
            applicationId = "com.example.nissan"
            resValue(type = "string", name = "app_name", value = "Nissan App")
            signingConfig = signingConfigs.getByName("nissanConfig")           
        }
    }
    
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

flutter {
    source = "../.."
}
