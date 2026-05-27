plugins {
    buildlogic.`kotlin-common-conventions-no-detekt`
    jacoco
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core:5.6.2")
    implementation("org.slf4j:slf4j-simple:2.0.16")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)
        xml.required.set(true)
    }
}
