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
    val flavor = System.getenv("FLAVOR_NAME") ?: throw GradleException("FLAVOR_NAME is not defined")

    signingConfigs {
        release{
            if (System.getenv("CI")) {
                storeFile = file(System.getenv("KEYSTORE_FILE") ?: throw GradleException("KEYSTORE_FILE is not defined"))
                storePassword = System.getenv("STORE_PASSWORD") ?: throw GradleException("STORE_PASSWORD is not defined")
                keyAlias = System.getenv("KEY_ALIAS") ?: throw GradleException("KEY_ALIAS is not defined")
                keyPassword = System.getenv("KEY_PASSWORD") ?: throw GradleException("KEY_PASSWORD is not defined")
            } else {
                storeFile = keystoreProperties["storeFile"]?.let { file(it as String) }
                storePassword = keystoreProperties["storePassword"] as String?
                keyAlias = keystoreProperties["keyAlias"] as String?
                keyPassword = keystoreProperties["keyPassword"] as String?
            }
        }
    }

    flavorDimensions("flavor-type")
    productFlavors {
        create("${flavor}") {
            dimension = "flavor-type"
            applicationId = "com.example.${flavor}"
            resValue(type = "string", name = "app_name", value = "${flavor} App")
            signingConfig = signingConfigs.release            
        }        
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.findByName("${clientName}CONFIG") ?: throw GradleException("${clientName}CONFIG not found")
        }
        debug {
            signingConfig = signingConfigs..debug"
        }
    }
}

flutter {
    source = "../.."
}
