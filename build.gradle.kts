plugins {
    java
    id("io.freefair.lombok") version ("4.1.6")
    id("com.github.johnrengelman.shadow") version ("5.2.0")
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

    testImplementation("org.apache.httpcomponents:httpclient:4.5.10")
    testImplementation("commons-io:commons-io:2.6")
    testImplementation("org.mockito:mockito-core:3.2.4")
    testImplementation("org.mockito:mockito-junit-jupiter:3.2.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    shadowJar {
        isZip64 = true
        manifest {
            attributes("Main-Class" to "com.revolut.Main")
        }
    }
}
