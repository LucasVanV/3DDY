plugins {
    id("com.android.application")
    id("org.sonarqube") version "4.2.1.3168"
}

android {
    namespace = "fr.uphf.a3ddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "fr.uphf.a3ddy"
        minSdk = 28
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation("commons-fileupload:commons-fileupload:1.5")
    implementation("androidx.security:security-crypto:1.1.0-alpha03")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    implementation("org.rajawali3d:rajawali:1.0.325@aar")

    runtimeOnly("androidx.annotation:annotation:1.7.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.fragment:fragment:1.6.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation("org.rajawali3d:rajawali:1.0.325@aar");

    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
}


sonar {
    properties {
        property ("sonar.projectKey", "3DDY_Android" )
        property ("sonar.projectName", "3DDY_Android")
        property ("sonar.host.url","http://localhost:9000")
        property ("sonar.token","sqp_7ccb99f15581fd2396242738f80b454bbfa7e91d")
    }
}
