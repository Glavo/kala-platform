import java.io.RandomAccessFile

plugins {
    `java-library`
    `maven-publish`
}

group = "org.glavo"
version = kalaVersion("0.4.0")

repositories {
    mavenCentral()
}

dependencies {
    "org.jetbrains:annotations:21.0.1".also {
        compileOnly(it)
        testImplementation(it)
    }

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
}

tasks.compileJava {
    modularity.inferModulePath.set(true)
    options.release.set(9)
    options.isWarnings = false
    doLast {
        val tree = fileTree(destinationDirectory)
        tree.include("**/*.class")
        tree.exclude("module-info.class")
        tree.forEach {
            RandomAccessFile(it, "rw").use { rf ->
                rf.seek(7)   // major version
                rf.write(51)   // java 7
                rf.close()
            }
        }
    }
}

java {
    withSourcesJar()
    // withJavadocJar()
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = project.name
            from(components["java"])

            pom {
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("glavo")
                        name.set("Glavo")
                        email.set("zjx001202@gmail.com")
                    }
                }
            }
        }
    }
}

tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).also {
        it.encoding("UTF-8")
        it.addStringOption("link", "https://docs.oracle.com/en/java/javase/11/docs/api/")
        it.addBooleanOption("html5", true)
        it.addStringOption("Xdoclint:none", "-quiet")
    }
}

fun kalaVersion(base: String) =
    if (System.getProperty("kala.release") == "true" || System.getenv("JITPACK") == "true")
        base
    else
        "$base-SNAPSHOT"