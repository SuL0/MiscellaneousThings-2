plugins {
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.serialization") version "1.5.20"
    id("kr.entree.spigradle") version "2.2.3"
    `java-library`
    `maven-publish`
    idea
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}


group = "kr.sul"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("http://repo.citizensnpcs.co/")
    maven("https://repo.md-5.net/content/groups/public/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://github.com/deanveloper/SkullCreator/raw/mvn-repo/")
    mavenLocal()
}

val pluginStorage = "C:/MC-Development/PluginStorage"
val nmsBukkitPath = "C:/MC-Development/마인즈서버/paper-1.12.2-R0.1-SNAPSHOT-shaded.jar"
val copyPluginDestination = "C:/MC-Development/마인즈서버/plugins"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    compileOnly(files(nmsBukkitPath))

    // ServerCore ShadowJar - ㄴㄴ 플러그인은 Shadow 할 생각 없음
    compileOnly("net.citizensnpcs:citizens-main:2.0.29-SNAPSHOT")
//    compileOnly("net.citizensnpcs:citizens-api:2.0.28-SNAPSHOT")
    compileOnly("net.luckperms:api:5.3")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0")
    compileOnly("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    compileOnly("org.mcmonkey:sentinel:2.4.0-SNAPSHOT")
    compileOnly("dev.dbassett:skullcreator:3.0.1")
    compileOnly("kr.sul:ServerCore:1.0-SNAPSHOT")
    compileOnly("me.sul:Parachute:1.0-SNAPSHOT")
    compileOnly("org.golde.bukkit.corpsereborn:CorpseReborn:1.0-SNAPSHOT")
    compileOnly("aaclans:AAClans:1.0-SNAPSHOT")
    compileOnly("kr.sul:CrackShot-2:1.0-SNAPSHOT")
    compileOnly("xyz.upperlevel.spigot.book:spigot-book-api:1.5")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.6.0")
    compileOnly("org.inventivetalent:glowapi:1.4.6-SNAPSHOT")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.6.0")

    compileOnly(files("$pluginStorage/EnderVaults-Bukkit-v1.0.13.jar"))
    compileOnly(files("$pluginStorage/ResourcepackSoundPlayer_S.jar"))
    compileOnly(files("$pluginStorage/Dependencies/HolographicDisplays.jar"))
    compileOnly(files("$pluginStorage/Dependencies/nbteditor-3.0.1.jar"))
    compileOnly(files("$pluginStorage/Dependencies/EffectLib-9.0.jar"))
//    compileOnly(files("$pluginStorage/helper.jar"))
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
        create("돈")
        create("인사하기")
        create("이동지점알림끄기")
        create("방어구수정")
    }
}

tasks {
    compileJava.get().options.encoding = "UTF-8"
    compileKotlin.get().kotlinOptions.jvmTarget = "1.8"
    compileTestKotlin.get().kotlinOptions.jvmTarget = "1.8"

    val copyPlugin = register<Copy>("copyPlugin") {
        from(files("$pluginStorage/${project.name}_S.jar"))
        into(file(copyPluginDestination))
    }

    jar {
        archiveFileName.set("${project.name}_S.jar")
        destinationDirectory.set(file(pluginStorage))
        finalizedBy(copyPlugin)
        finalizedBy(publishToMavenLocal)
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

// jar task 에 finalizedBy로 추가
// 관련 정보를 한국 Discord에 물어봤던 걸로 기억하는데
// -> .m2에 소스코드와 함께 저장
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group as String
            version = rootProject.version as String
            from(components["java"])
        }
    }
}