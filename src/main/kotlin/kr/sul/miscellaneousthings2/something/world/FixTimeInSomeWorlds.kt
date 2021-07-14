package kr.sul.miscellaneousthings2.something.world

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit

object FixTimeInSomeWorlds {
    private const val TERM = 10*20.toLong()
    init {
        Bukkit.getScheduler().runTask(plugin) {
            val worlds = Bukkit.getWorlds().filter { it.name.startsWith("Spawn", true) || it.name.startsWith("Normal", true) }
            val hardTestWorld = Bukkit.getWorld("HardTest")

            Bukkit.getScheduler().runTaskTimer(plugin, {
                // 낮, 비X 고정
                worlds.forEach {
                    it.time = 6000  // 낮
                    it.setStorm(false)  // 비 X
                }

                // 하드 테스트, 23시 고정, 비X
                hardTestWorld?.time = 23000
                hardTestWorld?.setStorm(false)  // 비 X
            }, 0, TERM)
        }
    }
}