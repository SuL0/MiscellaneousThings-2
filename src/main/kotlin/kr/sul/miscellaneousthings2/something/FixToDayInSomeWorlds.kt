package kr.sul.miscellaneousthings2.something

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit

object FixToDayInSomeWorlds {
    private const val TERM = 10*20.toLong()
    init {
        Bukkit.getScheduler().runTask(plugin) {
            val worlds = Bukkit.getWorlds().filter { it.name.startsWith("Spawn", true) || it.name.startsWith("Normal", true) }

            Bukkit.getScheduler().runTaskTimer(plugin, {
                worlds.forEach {
                    it.time = 6000  // 낮
                }
            }, 0, TERM)
        }
    }
}