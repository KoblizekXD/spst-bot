plugins {
    application
    id("com.gradleup.shadow") version "8.3.1"
}

application.mainClass = "dev.aa55h.Bot"
group = "dev.aa55h"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.25.2")
    implementation("com.sun.mail:jakarta.mail:2.0.2")
    implementation("net.spy:spymemcached:2.12.3")
    implementation("net.dv8tion:JDA:6.1.1")
    runtimeOnly("org.postgresql:postgresql:42.7.8")
}

tasks.test {
    useJUnitPlatform()
}