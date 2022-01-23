import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecraftforge.gradle.user.UserExtension

buildscript {
    repositories {
        maven("https://maven.minecraftforge.net") { name = "Forge" }
        maven("https://jitpack.io") { name = "jitpack.io" }
    }
    dependencies {
        classpath("com.github.GTNewHorizons:ForgeGradle:1.2.4")
    }
}

plugins {
    idea
    java
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

apply(plugin = "forge")

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

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val neSqlVersion: String by project
group = "com.github.dcysteine.nesql"
version = neSqlVersion

val minecraftVersion: String by project
val forgeVersion: String by project
minecraft.version = "$minecraftVersion-$forgeVersion-$minecraftVersion"

configure<UserExtension> {
    replacements.putAll(
        mapOf(
            Pair("@version@", version)
        )
    )
    runDir = "run"
}

val Project.minecraft: UserExtension
    get() = extensions.getByName<UserExtension>("minecraft")

val shadowImplementation: Configuration by configurations.creating {
    configurations["implementation"].extendsFrom(this)
}

val shadowRuntime: Configuration by configurations.creating {
    configurations["runtime"].extendsFrom(this)
}

repositories {
    maven("https://maven.minecraftforge.net") {
        name = "Forge"
        metadataSources { artifact() }
    }
    /*
    ivy {
        name = "gtnh_download_source"
        artifactPattern(
            "http://downloads.gtnewhorizons.com/Mods_for_Jenkins/[module]-[revision]-[classifier].[ext]")
        metadataSources { artifact() }
    }
     */
    maven("https://jitpack.io") { name = "jitpack.io" }
    maven("http://maven.ic2.player.to") {
        name = "IC2 & Forestry"
        metadataSources { artifact() }
    }
}

dependencies {
    // TODO delete this?
    //compileOnly(fileTree("libs") { include("*.jar") })

    val autoValueVersion: String by project
    compileOnly("com.google.auto.value:auto-value-annotations:$autoValueVersion")
    annotationProcessor("com.google.auto.value:auto-value:$autoValueVersion")

    val springDataVersion: String by project
    shadowImplementation("org.springframework.data:spring-data-jpa:$springDataVersion")

    val hibernateVersion: String by project
    shadowImplementation("org.hibernate:hibernate-core:$hibernateVersion")

    val h2Version: String by project
    shadowRuntime("com.h2database:h2:$h2Version")

    // Weird bug where Hibernate depends on this, but it doesn't get automatically included.
    // https://stackoverflow.com/questions/43249301/how-to-fix-classnotfoundexception-for-org-threeten-bp-localdatetime
    val threetenVersion: String by project
    shadowRuntime("org.threeten:threetenbp:$threetenVersion")

    val codeChickenCoreVersion: String by project
    val codeChickenLibVersion: String by project
    val neiVersion: String by project
    implementation("com.github.GTNewHorizons:CodeChickenCore:$codeChickenCoreVersion:dev")
    implementation("com.github.GTNewHorizons:CodeChickenLib:$codeChickenLibVersion:dev")
    implementation("com.github.GTNewHorizons:NotEnoughItems:$neiVersion:dev")

    val gregTech5Version: String by project
    compileOnly("com.github.GTNewHorizons:GT5-Unofficial:$gregTech5Version:dev") {
        isTransitive = false
    }

    val forestryVersion: String by project
    compileOnly("net.sengir.forestry:forestry_1.7.10:$forestryVersion:dev")

    /*
    // The following are compile-time dependencies of Forestry.
    val buildCraftVersion: String by project
    compileOnly("com.mod-buildcraft:buildcraft:$buildCraftVersion:dev")
     */

    /*
    // The following are compile-time dependencies of GT5.
    val enderIoVersion: String by project
    val forestryVersion: String by project
    val railcraftVersion: String by project
    compileOnly("crazypants.enderio:EnderIO-$minecraftVersion:${enderIoVersion}_beta:dev")
    compileOnly("net.sengir.forestry:forestry_$minecraftVersion:$forestryVersion:dev")
    compileOnly("mods.railcraft:Railcraft_$minecraftVersion:$railcraftVersion:dev")
     */
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

val devJar by tasks.creating(Jar::class) {
    from(sourceSets.main.get().output)
    archiveClassifier.set("dev")
}

artifacts {
    archives(depsJar)
    archives(sourcesJar)
    archives(devJar)
}