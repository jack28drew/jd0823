plugins {
    id("java")
    id("org.springframework.boot") version ("3.1.2")
    id("org.flywaydb.flyway") version "9.21.1"
}

group = "org.warehouse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.2")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.1.2")
    implementation("org.springframework.boot:spring-boot-starter-json:3.1.2")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.1.2")
    implementation("com.h2database:h2:2.2.220")
    implementation("org.flywaydb:flyway-core:9.21.1")
    implementation("org.javamoney:moneta:1.4.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.2")
    testImplementation("org.assertj:assertj-core:3.6.1")
}

sourceSets {
    create("intTest") {
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath + sourceSets["test"].runtimeClasspath
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.create<Test>("intTest") {
    useJUnitPlatform()
    description = "Runs integration tests"
    group = "verification"
    testClassesDirs = sourceSets["intTest"].output.classesDirs
    classpath = sourceSets["intTest"].runtimeClasspath
}

tasks.test {
    useJUnitPlatform()
}
