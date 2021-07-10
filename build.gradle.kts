plugins {
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.serialization") version "1.5.20"
    id("kr.entree.spigradle") version "2.2.3"
}

group = "kr.sul"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
//    maven("http://repo.citizensnpcs.co/")
    mavenLocal()
}

val pluginStorage = "C:/Users/PHR/Desktop/PluginStorage"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.2.2")
    compileOnly(files("C:/Users/PHR/Desktop/SERVER2/paper-1.12.2-R0.1-SNAPSHOT-shaded.jar"))


    compileOnly("net.luckperms", "api", "5.3")
    compileOnly("com.comphenix.protocol", "ProtocolLib", "4.6.0")
    compileOnly("net.citizensnpcs", "citizensapi", "2.0.28-SNAPSHOT")
//    compileOnly("net.citizensnpcs", "citizens-main", "2.0.28-SNAPSHOT")

    compileOnly(files("$pluginStorage/ResourcepackSoundPlayer_S.jar"))
    compileOnly(files("$pluginStorage/ServerCore_S.jar"))
    compileOnly(files("$pluginStorage/EnderVaults-Bukkit-v1.0.13.jar"))
    compileOnly(files("$pluginStorage/Dependencies/item-nbt-api-plugin-2.6.0.jar"))
}

spigot {
    authors = listOf("SuL")
    apiVersion = "1.12"
    version = project.version.toString()
    depends = listOf("ServerCore", "Citizens")
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

    val copyPlugin = register<Copy>("copyPlugin") {
        from(files("$pluginStorage/${project.name}_S.jar"))
        into(file("C:/Users/PHR/Desktop/마인즈서버/plugins"))
    }

    jar {
        archiveFileName.set("${project.name}_S.jar")
        destinationDirectory.set(file(pluginStorage))

        finalizedBy(copyPlugin)
    }
}
