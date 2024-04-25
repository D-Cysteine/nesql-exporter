pluginManagement {
    repositories {
        maven("https://nexus.gtnewhorizons.com/repository/public/") {
            name = "GTNH Maven"
            mavenContent {
                includeGroup("com.gtnewhorizons")
                includeGroupByRegex("com\\.gtnewhorizons\\..+")
            }
        }
        gradlePluginPortal()
    }
}
