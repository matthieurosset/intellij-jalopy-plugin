plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

group = "com.alexeyhanin.intellij"
version = "2.1.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2025.1")
        bundledPlugin("com.intellij.java")
        pluginVerifier()
        zipSigner()
    }

    // Jalopy dependencies
    implementation(files("libs/jalopy-1.10.jar"))
    implementation(files("libs/antlr-2.7.5.jar"))
    implementation(files("libs/log4j-1.2.8.jar"))
}

intellijPlatform {
    pluginConfiguration {
        id = "com.alexeyhanin.intellij.jalopyplugin"
        name = "Jalopy Plugin"
        version = project.version.toString()
        description = """
            <h1>Jalopy Code Formatter Plugin</h1>

            <p>Use Ctrl+Shift+P to format currently active document, or alternatively, enable automatic formatting on save in
            plugin configuration.</p>

            <h2>Features</h2>
            <ul>
                <li>Manual formatting with keyboard shortcut</li>
                <li>Automatic formatting on file save</li>
                <li>Custom Jalopy profile (convention XML) support</li>
            </ul>

            <h2>Compatibility</h2>
            <p>Compatible with IntelliJ IDEA 2025.1+</p>
        """.trimIndent()

        vendor {
            name = "Alexey Hanin"
            email = "mail@alexeyhanin.com"
            url = "https://github.com/ahanin"
        }

        ideaVersion {
            sinceBuild = "251"
            untilBuild = provider { null }
        }
    }

    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    patchPluginXml {
        sinceBuild = "251"
        untilBuild = provider { null }
    }
}
