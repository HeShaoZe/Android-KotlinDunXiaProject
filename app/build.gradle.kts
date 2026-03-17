plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.dunxiaweather"
    compileSdk = 34

    signingConfigs {
        create("release") {
            storeFile = file("../dunxiasign.jks")
            storePassword = "mima"
            keyAlias = "mima"
            keyPassword = "mima"
        }
    }

    defaultConfig {
        applicationId = "com.example.dunxiaweather"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    productFlavors {
        create("baidu") {
            dimension = "version"
            applicationIdSuffix = ""

            buildConfigField("String", "CHANNEL_ID","\"sfsfsdfsfsdfs\"")
            buildConfigField("String", "CAMPAIGN_ID","\"sfsdfs\"")

            // 方法 B: 或者注入到 AndroidManifest.xml (如果原生代码通过 metaData 读取)
//            manifestPlaceholders.putAll(mapOf(
//                "UMENG_CHANNEL" to "BDSJDWZJMYSS3", // 友盟等统计 SDK 常用
//                "CHANNEL_CODE" to "BDSJDWZJMYSS3",
//                "AD_PLAN_ID" to "8829"
//            ))
        }

        create("default") {
            dimension = "version"
            buildConfigField("String", "CHANNEL_ID", "\"DEFAULT\"")
            buildConfigField("String", "CAMPAIGN_ID", "\"0\"")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.google.android.material:material:1.12.0")

    // OkHttp 核心库
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // 如果你需要解析 JSON，通常配合 Moshi 或 Gson
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")

    // Kotlin 协程
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")


    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    // 如果使用 Glide 加载图片
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    ///音视频播放
    // 核心播放器
    implementation("androidx.media3:media3-exoplayer:1.3.0")
    // 默认的 UI 组件 (包含进度条、播放/暂停按钮、全屏等)
    implementation("androidx.media3:media3-ui:1.3.0")
    // 如果需要支持 HLS (.m3u8) 或 DASH，通常核心包里已包含，若需特殊格式可额外添加
    implementation("androidx.media3:media3-exoplayer-hls:1.3.0")

    // 添加 SplashScreen 兼容库
    implementation("androidx.core:core-splashscreen:1.0.1")
}