pluginManagement {
    repositories {
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "scratches"

include(":eo")
include(":json-input")
include(":spring-integration:message-serde")
include(":poc")
include(":conversation")
