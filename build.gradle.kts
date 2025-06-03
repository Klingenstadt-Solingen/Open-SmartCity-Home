import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.sentry)
    alias(libs.plugins.cyclonedx.bom)
}

group = "de.osca.fama"
version = project.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.krossbow.stomp)
    implementation(libs.bundles.kmqtt)
    implementation(libs.bundles.kermit)

    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    implementation(libs.kotlin.ktor.cio)

    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.ktor.test)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit5)
    testImplementation(libs.mockk)
}

application {
    mainClass = "de.osca.fama.MainKt"
}

kotlin {
    sourceSets {
        val main by getting {
            kotlin.srcDir("build/generated/src/main/kotlin")
        }
    }
}

tasks.jar {
    manifest.attributes["Main-Class"] = application.mainClass
    val dependencies =
        configurations
            .runtimeClasspath
            .get()
            .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test {
    useJUnitPlatform()
    jvmArgs(
        "--add-opens",
        "java.base/java.util=ALL-UNNAMED",
    )
}

kover {
    reports {
        filters {
            excludes {
                classes("de.osca.fama.generated.*")
            }
        }
    }
}

ktlint {
    outputToConsole = true
    outputColorName = "RED"
    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
}

sentry {
    includeSourceContext = true

    org = "open-smart-city"
    projectName = "home"
    authToken = System.getenv("SENTRY_AUTH_TOKEN")
    autoInstallation {
        enabled = true
    }
}

apply("buildconfig.gradle.kts")
