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
    implementation("org.hibernate:hibernate-core:5.4.10.Final")

    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}
