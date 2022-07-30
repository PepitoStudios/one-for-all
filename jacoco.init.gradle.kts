tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

private val classDirectoriesTree = fileTree("${project.buildDir}") {
    include(
        "**/classes/**/main/**",
        "**/intermediates/classes/debug/**",
        "**/intermediates/javac/debug/*/classes/**", // Android Gradle Plugin 3.2.x support.
        "**/tmp/kotlin-classes/debug/**"
    )
    exclude(
        "**/R.class",
        "**/R\$*.class",
        "**/di/**",
        // "**/*\$1*",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/models/**",
        "**/*\$Lambda$*.*",
        "**/*\$inlined$*.*"
    )
}

private val sourceDirectoriesTree = files("$projectDir/src/main/java")

private val executionDataTree = fileTree("${project.buildDir}") {
    include(
        "outputs/code_coverage/**/*.ec",
        "unit_test_code_coverage/**/testDebugUnitTest.exec"
    )
}

fun JacocoReportsContainer.reports() {
    csv.required.set(false)
    xml.apply {
        required.set(false)
        outputLocation.set(file("$buildDir/reports/code-tururu/xml"))
    }
    html.apply {
        required.set(true)
        outputLocation.set(file("$buildDir/reports/code-tururu/html"))
    }
}

fun JacocoReport.setDirectories() {
    sourceDirectories.setFrom(sourceDirectoriesTree)
    classDirectories.setFrom(classDirectoriesTree)
    executionData.setFrom(executionDataTree)
}

fun JacocoCoverageVerification.setDirectories() {
    sourceDirectories.setFrom(sourceDirectoriesTree)
    classDirectories.setFrom(classDirectoriesTree)
    executionData.setFrom(executionDataTree)
}

val jacocoGroup = "verification"
tasks.register<JacocoReport>("jacocoTestReport") {
    group = jacocoGroup
    description = "Code coverage report for both Android and Unit tests."
    dependsOn("testDebugUnitTest")
    //dependsOn("createDebugCoverageReport")
    reports {
        reports()
    }
    setDirectories()
}

val minimumCoverage = "0.8".toBigDecimal()
tasks.register<JacocoCoverageVerification>("jacocoCoverageVerification") {
    group = jacocoGroup
    description = "Code coverage verification for Android both Android and Unit tests."
    dependsOn("testDebugUnitTest")
    dependsOn("createDebugCoverageReport")
    violationRules {
        rule {
            limit {
                minimum = minimumCoverage
            }
        }
        rule {
            element = "CLASS"
            excludes = listOf(
                "**.FactorFacade.Builder",
                "**.ServiceFacade.Builder",
                "**.ChallengeFacade.Builder",
                "**.Task"
            )
            limit {
                minimum = minimumCoverage
            }
        }
    }
    setDirectories()
}
