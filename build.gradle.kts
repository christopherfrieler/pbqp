import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm") version "1.5.21"
    id("org.jetbrains.dokka") version "1.5.0"
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
