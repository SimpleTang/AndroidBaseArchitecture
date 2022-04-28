// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    apply {
        "from" to "buildVers.gradle.kts"
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.tyl.plugin:plugin_autoRegister:1.0.0")
    }

    repositories {
        google()
        jcenter()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url = uri("https://repo1.maven.org/maven2/")
        }
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url = uri("https://repo1.maven.org/maven2/")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}