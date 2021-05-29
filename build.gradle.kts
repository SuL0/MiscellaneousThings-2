plugins {
    kotlin("jvm") version "1.4.32"
    id("kr.entree.spigradle") version "2.2.3"
}

group = "kr.sul"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    mavenLocal()
}

val pluginStorage = "C:/Users/PHR/Desktop/PluginStorage"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("com.destroystokyo.paper", "paper-api", "1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.12.2-R0.1-SNAPSHOT")

    compileOnly("net.luckperms", "api", "5.3")

    compileOnly(files("$pluginStorage/ResourcepackSoundPlayer_S.jar"))
    compileOnly(files("$pluginStorage/ServerCore_S.jar"))
    compileOnly(files("$pluginStorage/Dependencies/item-nbt-api-plugin-2.6.0.jar"))
    compileOnly(files("C:/Users/PHR/Desktop/마인즈서버/plugins/EnderVaults-Bukkit-1.0.12.jar"))
}

spigot {
    authors = listOf("SuL")
    apiVersion = "1.12"
    version = project.version.toString()
    depends = listOf("ServerCore")
    softDepends = listOf("EnderVaults")
    commands {
        create("nbtview") {
            permission = "op.op"
            description = "아이템의 특정 NBT 값을 확인합니다."
        }
    }
}

tasks {
    compileJava.get().options.encoding = "UTF-8"
    compileKotlin.get().kotlinOptions.jvmTarget = "1.8"
    compileTestKotlin.get().kotlinOptions.jvmTarget = "1.8"

    jar {
        archiveFileName.set("${project.name}_S.jar")
        destinationDirectory.set(file(pluginStorage))
    }
}
