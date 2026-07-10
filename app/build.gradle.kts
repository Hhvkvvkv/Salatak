plugins {
    id("com.android.application")
}

android {
    namespace = "com.salatak.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.salatak.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 42
        versionName = "4.1.1"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // AndroidX Core
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity:1.8.2")
    implementation("androidx.fragment:fragment:1.6.2")
    implementation("androidx.core:core:1.12.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")

    // Media3 (ExoPlayer - تشغيل القرآن والراديو)
    implementation("androidx.media3:media3-exoplayer:1.2.1")
    implementation("androidx.media3:media3-session:1.2.1")
    implementation("androidx.media3:media3-datasource:1.2.1")
    implementation("androidx.media3:media3-extractor:1.2.1")

    // Material Design
    implementation("com.google.android.material:material:1.11.0")

    // Volley (طلبات الشبكة)
    implementation("com.android.volley:volley:1.2.1")

    // Adhan (حساب مواقيت الصلاة)
    implementation("com.batoulapps.adhan:adhan:2.0.1")
}
