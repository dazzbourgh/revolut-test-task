plugins {
    java
    id("io.freefair.lombok") version("4.1.6")
}

group = "com.revolut"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:2.8.0")
    implementation("org.slf4j:slf4j-simple:1.7.26")
    implementation("com.google.inject:guice:4.2.2")
    implementation("com.google.inject.extensions:guice-multibindings:4.2.2")
    implementation("com.google.code.gson:gson:2.8.6")

    testImplementation("org.mockito:mockito-core:2.26.0")
    testImplementation("org.mockito:mockito-junit-jupiter:2.26.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
}
