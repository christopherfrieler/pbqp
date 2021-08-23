import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm") version "1.5.21"
    id("org.jetbrains.dokka") version "1.5.0"
    jacoco
    id("org.sonarqube") version "3.3"
}

group = "rocks.frieler.pbqp"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    test {
        useJUnitPlatform()
    }
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)
        }
    }

    register("sourcesJar", Jar::class) {
        from(project.sourceSets["main"].allJava.srcDirs)
        archiveClassifier.set("sources")
    }

    register("javadocJar", Jar::class) {
        dependsOn(dokkaJavadoc)
        from("${buildDir}/dokka/javadoc")
        archiveClassifier.set("javadoc")
    }
}

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "christopherfrieler")
        property("sonar.projectName", "pbqp")
        property("sonar.projectKey", "christopherfrieler_pbqp")
        when (val analysisType = System.getenv("SONAR_ANALYSIS_TYPE")) {
            "branch" -> property("sonar.branch.name", System.getenv("SONAR_BRANCH_NAME"))
            "pull_request" -> property("sonar.pullrequest.key", System.getenv("SONAR_PULLREQUEST_KEY"))
            else -> logger.warn("unknown SONAR_ANALYSIS_TYPE: '{}'", analysisType)
        }
    }
}
tasks.sonarqube {
    dependsOn(tasks.jacocoTestReport)
}
