plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.spring.core)
    implementation(libs.spring.context)
    implementation(libs.spring.tx)
}