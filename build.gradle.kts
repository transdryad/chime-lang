plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.6"
}

group = "org.hazelv"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

sourceSets {
    main {
        java {
            srcDir("src/main/java")
            resources.srcDir("src/main/resources")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "Main")
    }
}
