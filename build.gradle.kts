import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotestVersion: String by project
val mockkVersion: String by project
val ktor: String by project
val kotlin: String by project
val logback: String by project
val koinKtor: String by project
val pgDriverVersion: String by project
val jdbiVersion: String by project
val hikariVersion: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.3"
    kotlin("plugin.serialization") version "1.4.32"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

fun DependencyHandlerScope.jdbi() {
    implementation("org.postgresql:postgresql:$pgDriverVersion")
    implementation("org.jdbi:jdbi3-core:$jdbiVersion")
    implementation("org.jdbi:jdbi3-sqlobject:$jdbiVersion")
    implementation("org.jdbi:jdbi3-kotlin:$jdbiVersion")
    implementation("org.jdbi:jdbi3-postgres:$jdbiVersion")
    implementation("com.zaxxer:HikariCP:$hikariVersion")
}
dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor")
    implementation("io.ktor:ktor-client-core:$ktor")
    implementation("io.ktor:ktor-client-okhttp:$ktor")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor")
    implementation("ch.qos.logback:logback-classic:$logback")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.insert-koin:koin-ktor:$koinKtor")
    implementation("io.insert-koin:koin-logger-slf4j:$koinKtor")
    jdbi()
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    implementation(kotlin("stdlib-jdk8"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
tasks {
    shadowJar {
        isZip64 = true
        manifest {
            attributes["Main-Class"] = "com.example.ApplicationKt"
        }
        transform(com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer::class.java)
    }

    test {
        useJUnitPlatform()

        testLogging {
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }

    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjvm-default=enable")
            allWarningsAsErrors = false
            jvmTarget = "17"
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}
