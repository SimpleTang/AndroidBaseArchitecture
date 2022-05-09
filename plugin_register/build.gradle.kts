buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
    }
}

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-stdlib:1.3.72"))
    implementation(platform("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72"))
    implementation(platform("org.jetbrains.kotlin:kotlin-reflect:1.3.72"))
    implementation(platform("org.jetbrains.kotlin:kotlin-stdlib-common:1.3.72"))
    implementation("com.android.tools.build:gradle:4.2.1")
    implementation("org.ow2.asm:asm:9.1")
    implementation("org.ow2.asm:asm-util:9.1")
    implementation("org.ow2.asm:asm-commons:9.1")
    implementation(gradleApi())
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

group = "com.tyl.aba"
version = "1.0.0"

gradlePlugin {
    plugins {
        create("register") {
            id = "com.tyl.aba.plugin.register"// plugins id
            implementationClass = "com.tyl.plugin.autoregister.AutoRegisterPlugin"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/simpletang/AndroidBaseArchitecture")
            credentials {
                username = project.findProperty("gpr.user") as String?
                password = project.findProperty("gpr.key") as String?
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
