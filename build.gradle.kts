import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    idea
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.google.protobuf") version "0.9.4"
    id("com.gtnewhorizons.retrofuturagradle") version "1.3.35"
}

val projectJavaVersion = JavaLanguageVersion.of(8)

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.26.1"
    }
}

val nesqlExporterVersion: String by project
group = "com.github.dcysteine.nesql.exporter"
version = nesqlExporterVersion

val minecraftVersion: String by project
minecraft {
    mcVersion.set(minecraftVersion)

    injectedTags.put("EXPORTER_VERSION", project.version)
}

val shadowImplementation: Configuration by configurations.creating {
    configurations["implementation"].extendsFrom(this)
}

val shadowRuntime: Configuration by configurations.creating {
    configurations["runtimeOnly"].extendsFrom(this)
}

repositories {
    maven("https://nexus.gtnewhorizons.com/repository/public/") {
        name = "GTNH Maven"
    }
    maven("https://cursemaven.com") {
        name = "Curse Maven"
    }

    /*
     * This repo seems to be broken...
    maven("https://maven.ic2.player.to") {
        name = "IC2 Maven"
        metadataSources {
            mavenPom()
            artifact()
        }
        content {
            includeGroup("net.industrial-craft")
        }
    }
     */
    maven("https://maven2.ic2.player.to") {
        name = "IC2 Maven2"
        metadataSources {
            mavenPom()
            artifact()
        }
        content {
            includeGroup("net.industrial-craft")
        }
    }

    maven("https://gregtech.overminddl1.com") {
        content {
            includeGroup("thaumcraft")
        }
    }
}

dependencies {
    val autoValueVersion: String by project
    compileOnly("com.google.auto.value:auto-value-annotations:$autoValueVersion")
    annotationProcessor("com.google.auto.value:auto-value:$autoValueVersion")

    val protoBufferVersion: String by project
    shadowImplementation("com.google.protobuf:protobuf-java:$protoBufferVersion")

    val lombokVersion: String by project
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    val springDataVersion: String by project
    shadowImplementation("org.springframework.data:spring-data-jpa:$springDataVersion")

    val jakartaPersistenceVersion: String by project
    compileOnly("jakarta.persistence:jakarta.persistence-api:$jakartaPersistenceVersion")

    val hibernateVersion: String by project
    shadowImplementation("org.hibernate:hibernate-core-jakarta:$hibernateVersion")
    annotationProcessor("org.hibernate:hibernate-jpamodelgen-jakarta:$hibernateVersion")

    val hsqldbVersion: String by project
    shadowRuntime("org.hsqldb:hsqldb:$hsqldbVersion:jdk8")

    val neiVersion: String by project
    implementation("com.github.GTNewHorizons:NotEnoughItems:$neiVersion:dev")

    val mobsInfoVersion: String by project
    implementation("com.github.GTNewHorizons:Mobs-Info:$mobsInfoVersion:dev")

    val avaritiaVersion: String by project
    implementation("com.github.GTNewHorizons:Avaritia:$avaritiaVersion:dev")

    val gregTech5Version: String by project
    implementation("com.github.GTNewHorizons:GT5-Unofficial:$gregTech5Version:dev")

    val thaumcraftVersion: String by project
    implementation("thaumcraft:Thaumcraft:$minecraftVersion-$thaumcraftVersion:dev")
    val thaumcraftNeiVersion: String by project
    implementation("curse.maven:thaumcraft-nei-plugin-225095:$thaumcraftNeiVersion")

    val betterQuestingVersion: String by project
    implementation("com.github.GTNewHorizons:BetterQuesting:$betterQuestingVersion:dev")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Jar> {
    // Replace version in mcmod.info
    filesMatching("mcmod.info") {
        expand(
            mapOf(
                "version" to project.version,
                "mcversion" to project.minecraft.version
            )
        )
    }
    archiveBaseName.set("NESQL-Exporter")
}

tasks.injectTags.configure {
    outputClassName.set("com.github.dcysteine.nesql.Tags")
}

// Unfortunately, we can neither minimize the shadow jar nor relocate it,
// because Hibernate seems to reference classes indirectly and so we would break it.
//
// We also can't relocate it because it does not include the mod code, so we would need to relocate
// the code separately.
//
// I had to make this a separate deps jar rather than a single shadow jar that contains both the
// deps and the mod code. The reason appears to be some kind of weird bug where trying to include
// the mod code causes org.slf4j, specifically, to not get picked up somehow, resulting in a
// ClassNotFoundException at runtime. Oddly enough, copying that single directory verbatim out of
// the shadow jar and into a separate jar fixes the issue.
//
// I spent way too long trying to figure out what went wrong, so I'm giving up and making this a
// separate jar. This does have the side benefit of speeding up build times, since deps don't change
// very often.
val depsJar by tasks.creating(ShadowJar::class) {
    // If mod code were to actually be included, we'd need this to use the obfuscated mod code.
    //from(tasks["reobf"].outputs)
    configurations = listOf(shadowImplementation, shadowRuntime)

    /*
     * Doesn't look like we actually need class path.
    manifest {
        val classPath = (shadowImplementation + shadowRuntime).joinToString(" ") { it.name }
        attributes("Class-Path" to classPath)
    }
     */

    // Remove mod code and other junk from jar.
    val excludeFun =
        fun(fileTreeElement: FileTreeElement): Boolean {
            val path = fileTreeElement.path
            val keep = path.endsWith(".jar")
            val remove = !path.contains("/") || path.startsWith("META-INF") || path.startsWith(project.group.toString())
            return remove && !keep
        }
    exclude(excludeFun)

    archiveClassifier.set("deps")
}

val sourcesJar by tasks.creating(Jar::class) {
    from(sourceSets.main.get().allSource)
    from("$projectDir/LICENSE.md")
    archiveClassifier.set("sources")
}

// Export SQL Schema for NESQL Server.
val sqlJar by tasks.creating(Jar::class) {
    from(sourceSets.main.get().output)
    from(sourceSets.injectedTags.get().output)
    exclude("com/github/dcysteine/nesql/exporter")
    exclude("*.proto")
    exclude("META-INF")
    exclude("mcmod.info")
    archiveClassifier.set("sql")
}

artifacts {
    archives(depsJar)
    archives(sourcesJar)
    archives(sqlJar)
}