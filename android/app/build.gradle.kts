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

    val clientName = System.getenv("CLIENT_NAME") ?: throw GradleException("CLIENT_NAME is not defined")

    signingConfigs {
        create("${clientName}CONFIG") {
            if (System.getenv("CI") != null) {
                storeFile = file(System.getenv("${clientName}_KEYSTORE_PATH") ?: throw GradleException("${clientName}_KEYSTORE_PATH is not defined"))
                storePassword = System.getenv("${clientName}_KEYSTORE_PASSWORD") ?: throw GradleException("${clientName}_KEYSTORE_PASSWORD is not defined")
                keyAlias = System.getenv("${clientName}_KEY_ALIAS") ?: throw GradleException("${clientName}_KEY_ALIAS is not defined")
                keyPassword = System.getenv("${clientName}_KEY_PASSWORD") ?: throw GradleException("${clientName}_KEY_PASSWORD is not defined")
            } else {
                storeFile = keystoreProperties["${clientName}_storeFile"]?.let { file(it as String) }
                storePassword = keystoreProperties["${clientName}_storePassword"] as String?
                keyAlias = keystoreProperties["${clientName}_keyAlias"] as String?
                keyPassword = keystoreProperties["${clientName}_keyPassword"] as String?
            }
        }
    }

    flavorDimensions("flavor-type")
    productFlavors {
        create("mazda") {
            dimension = "flavor-type"
            applicationId = "com.example.mazda"
            resValue(type = "string", name = "app_name", value = "Mazda App")
            signingConfig = signingConfigs.getByName("MAZDACONFIG")            
        }
        create("nissan") {
            dimension = "flavor-type"
            applicationId = "com.example.nissan"
            resValue(type = "string", name = "app_name", value = "Nissan App")
            signingConfig = signingConfigs.getByName("NISSANCONFIG")           
        }
    }
    
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.findByName("${clientName}CONFIG") ?: throw GradleException("${clientName}CONFIG not found")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

flutter {
    source = "../.."
}
