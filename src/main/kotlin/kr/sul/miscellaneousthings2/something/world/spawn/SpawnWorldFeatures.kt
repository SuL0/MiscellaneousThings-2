package kr.sul.miscellaneousthings2.something.world.spawn

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.ClassifyWorlds
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.Listener
import java.io.File
import kotlin.random.Random

object SpawnWorldFeatures: Listener {
    val RENDER_DISTANCE = run {
        val file = File("${Bukkit.getServer().worldContainer}/spigot.yml")
        val yaml = YamlConfiguration.loadConfiguration(file)
        return@run yaml.getInt("world-settings.default.entity-tracking-range.players")
    }

    init {
        // 파티클 장식
        Bukkit.getScheduler().runTaskTimer(plugin, {
            ClassifyWorlds.spawnWorlds.forEach { spawnWorld ->
                // 엔더 창고 - 1칸 블럭
                if (Random.nextBoolean()) {
                    spawnWorld.spawnParticle(Particle.VILLAGER_HAPPY, 1642.5, 51.5, 568.5,
                        2, 0.6, 0.6, 0.6, 1.0, null)
                }

                // 가상창고 - 2칸 블럭
                if (Random.nextBoolean()) {
                    if (Random.nextDouble() <= 0.8) {
                    spawnWorld.spawnParticle(Particle.VILLAGER_HAPPY, 1650.5, 51.5, 562.5,
                        2, 0.6, 0.6, 0.6, 1.0, null)
                    }
                    if (Random.nextDouble() <= 0.8) {
                        spawnWorld.spawnParticle(Particle.VILLAGER_HAPPY, 1649.5, 51.5, 562.5,
                            2, 0.6, 0.6, 0.6, 1.0, null)
                    }
                }
            }
        }, 5L, 5L)
    }
}