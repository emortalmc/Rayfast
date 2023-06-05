plugins {
    java
    `maven-publish`
}

group = "dev.emortal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

publishing {
    repositories {
        maven {
            name = "snapshots"
            url = uri("https://repo.emortal.dev/snapshots")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_SECRET")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.emortal"
            artifactId = "rayfast"

            val commitHash = System.getenv("COMMIT_HASH_SHORT")
            val releaseVersion = System.getenv("RELEASE_VERSION")
            version = commitHash ?: releaseVersion ?: "local"

            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
