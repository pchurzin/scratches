plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(libs.jackson.core.databind)
    api(libs.spring.core)
    api(libs.spring.messaging)

    implementation(libs.jackson.module.kotlin)
}