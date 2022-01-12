package kr.sul.miscellaneousthings2.warptobeachtown

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.extensionfunction.WorldPlayers.getNearbyRealPlayers
import kr.sul.servercore.util.ClassifyWorlds
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object FountainOfLife {
    private val fountainList = arrayListOf<Location>()
    init {
        init()
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (fountainLoc in fountainList) {
                fountainLoc.world.spawnParticle(Particle.VILLAGER_HAPPY, fountainLoc, 5, 1.0, 0.5, 1.0)
            }
        }, 10L, 10L)
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (fountainLoc in fountainList) {
                for (nearbyP in fountainLoc.getNearbyRealPlayers(3.5)) {
                    nearbyP.removePotionEffect(PotionEffectType.REGENERATION)
                    nearbyP.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 79, 1, true, false))
                }
            }
        }, 20L, 30L)
    }

    private fun init() {
        Bukkit.getScheduler().runTask(plugin) {
            fountainList.clear()

            val beachTown1 = ClassifyWorlds.beachTownWorlds.first()
            fountainList.run {
                add(Location(beachTown1, 696.0, 57.3, -67.0))
                add(Location(beachTown1, 452.0, 123.3, 283.0))
                add(Location(beachTown1, 157.0, 66.3, 329.0))
                add(Location(beachTown1, 282.0, 53.3, -23.0))
                add(Location(beachTown1, 275.5, 57.3, -131.0))
                add(Location(beachTown1, 307.5, 53.3, -232.5))
            }
        }
    }

}