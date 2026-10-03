import java.util.Base64
import com.google.gms.googleservices.GoogleServicesPlugin.MissingGoogleServicesStrategy

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.roborazzi)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.services)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.aistudio.cleankrpartner.kzqwm"
    minSdk = 24
    targetSdk = 37
    versionCode = 2
    versionName = "1.0.1"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    val keystorePath = System.getenv("KEYSTORE_PATH")
    val storePasswordEnv = System.getenv("STORE_PASSWORD")
    val keyPasswordEnv = System.getenv("KEY_PASSWORD")
    val keyAliasEnv = System.getenv("KEY_ALIAS") ?: "upload"

    if (!keystorePath.isNullOrEmpty() && file(keystorePath).exists()) {
      create("release") {
        storeFile = file(keystorePath)
        storePassword = storePasswordEnv
        keyAlias = keyAliasEnv
        keyPassword = keyPasswordEnv
      }
    } else if (file("${rootDir}/my-upload-key.jks").exists()) {
      create("release") {
        storeFile = file("${rootDir}/my-upload-key.jks")
        storePassword = storePasswordEnv
        keyAlias = keyAliasEnv
        keyPassword = keyPasswordEnv
      }
    } else {
      create("release") {
        val rootDebugKeystore = file("${rootDir}/debug.keystore")
        val base64Keystore = file("${rootDir}/debug.keystore.base64")
        if (!rootDebugKeystore.exists() && base64Keystore.exists()) {
          try {
            val bytes = Base64.getDecoder().decode(base64Keystore.readText().trim())
            rootDebugKeystore.writeBytes(bytes)
          } catch (_: Exception) {}
        }
        if (rootDebugKeystore.exists()) {
          storeFile = rootDebugKeystore
          storePassword = "android"
          keyAlias = "androiddebugkey"
          keyPassword = "android"
        } else {
          val homeKeystore = file("${System.getProperty("user.home")}/.android/debug.keystore")
          if (homeKeystore.exists()) {
            storeFile = homeKeystore
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
          } else {
            initWith(getByName("debug"))
          }
        }
      }
    }
    create("debugConfig") {
      val rootDebugKeystore = file("${rootDir}/debug.keystore")
      val base64Keystore = file("${rootDir}/debug.keystore.base64")
      if (!rootDebugKeystore.exists() && base64Keystore.exists()) {
        try {
          val bytes = Base64.getDecoder().decode(base64Keystore.readText().trim())
          rootDebugKeystore.writeBytes(bytes)
        } catch (_: Exception) {}
      }
      if (rootDebugKeystore.exists()) {
        storeFile = rootDebugKeystore
        storePassword = "android"
        keyAlias = "androiddebugkey"
        keyPassword = "android"
      } else {
        initWith(getByName("debug"))
      }
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug { signingConfig = signingConfigs.getByName("debugConfig") }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
}

secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
  ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
}

googleServices { missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN }

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(platform(libs.firebase.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.coil.compose)
  implementation(libs.converter.moshi)
  implementation(libs.firebase.firestore)
  implementation(libs.firebase.auth)
  implementation(libs.firebase.messaging)
  implementation(libs.firebase.storage)
  implementation(libs.androidx.browser)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  implementation(libs.retrofit)

  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.roborazzi)
  testImplementation(libs.roborazzi.compose)
  testImplementation(libs.roborazzi.junit.rule)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}
