import java.io.RandomAccessFile
import java.util.Properties

plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "org.glavo.kala"
version = "0.8.0"// + "-SNAPSHOT"

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
    withJavadocJar()
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}


loadMavenPublishProperties()

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = project.name
            from(components["java"])

            pom {
                name.set(project.name)
                description.set("Kala Platform")
                url.set("https://github.com/Glavo/kala-platform")
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
                scm {
                    url.set("https://github.com/Glavo/kala-platform")
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

fun loadMavenPublishProperties() {
    var secretPropsFile = project.rootProject.file("gradle/maven-central-publish.properties")
    if (!secretPropsFile.exists()) {
        secretPropsFile =
            file(System.getProperty("user.home")).resolve(".gradle").resolve("maven-central-publish.properties")
    }

    if (secretPropsFile.exists()) {
        // Read local.properties file first if it exists
        val p = Properties()
        secretPropsFile.reader().use {
            p.load(it)
        }

        p.forEach { (name, value) ->
            rootProject.ext[name.toString()] = value
        }
    }

    listOf(
        "sonatypeUsername" to "SONATYPE_USERNAME",
        "sonatypePassword" to "SONATYPE_PASSWORD",
        "sonatypeStagingProfileId" to "SONATYPE_STAGING_PROFILE_ID",
        "signing.keyId" to "SIGNING_KEY_ID",
        "signing.password" to "SIGNING_PASSWORD",
        "signing.key" to "SIGNING_KEY"
    ).forEach { (p, e) ->
        if (!rootProject.ext.has(p)) {
            rootProject.ext[p] = System.getenv(e)
        }
    }
}

signing {
    useInMemoryPgpKeys(
        rootProject.ext["signing.keyId"].toString(),
        rootProject.ext["signing.key"].toString(),
        rootProject.ext["signing.password"].toString(),
    )
    sign(publishing.publications["maven"])
}

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set(rootProject.ext["sonatypeStagingProfileId"].toString())
            username.set(rootProject.ext["sonatypeUsername"].toString())
            password.set(rootProject.ext["sonatypePassword"].toString())
        }
    }
}
