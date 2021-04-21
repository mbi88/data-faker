import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("ru.vyarus.quality").version("4.5.0")
    id("java-library")
    id("jacoco")
}
apply(plugin = "maven-publish")

val suitesDir = "src/test/resources/suites/"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.testng", "testng", "7.3.0")
    implementation("joda-time", "joda-time", "2.10.9")
    implementation("org.json", "json", "20201115")
    implementation("org.apache.commons", "commons-lang3", "3.11")
}

tasks.test {
    useTestNG {
        // Add test suites
        File(projectDir.absolutePath + "/" + suitesDir)
                .walk()
                .forEach {
                    if (it.isFile) {
                        suites(it)
                    }
                }

        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
        }
    }
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
        html.destination = layout.buildDirectory.dir("${buildDir}/reports/coverage").get().asFile
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

quality {
    checkstyle = true
    pmd = true
    codenarc = true
    spotbugs = true
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}
