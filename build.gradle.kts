plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

//dependencies {
//    testImplementation(platform("org.junit:junit-bom:5.9.1"))
////    testImplementation("org.junit.jupiter:junit-jupiter")
//    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
//    testImplementation("org.hamcrest:hamcrest-all:1.3")
//    implementation("org.seleniumhq.selenium:selenium-java:4.16.1")
//    implementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
//}

//ext {
//    allureVersion = '2.24.0'
//    junitVersion = '5.10.0'
//}

dependencies {
    testImplementation("org.seleniumhq.selenium:selenium-java:4.16.1")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.junit.platform:junit-platform-suite-engine:1.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-reporting:1.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test>().configureEach {
    val outputDir = reports.junitXml.outputLocation
    jvmArgumentProviders += CommandLineArgumentProvider {
        listOf(
                "-Djunit.platform.reporting.open.xml.enabled=true",
                "-Djunit.platform.reporting.output.dir=${outputDir.get().asFile.absolutePath}"
        )
    }
}
tasks.test {
    useJUnitPlatform()
}