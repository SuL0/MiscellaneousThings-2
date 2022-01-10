package kr.sul.miscellaneousthings2.something

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.extensionfunction.WorldPlayers.getRealPlayersExcept
import kr.sul.servercore.util.ClassifyWorlds
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.inventivetalent.glow.GlowAPI

// TODO 노말 테섭용
/*
object GlowAllPlayersInNormal: Listener {

    @EventHandler
    fun onJoinInNormalWorld(e: PlayerJoinEvent) {
        if (ClassifyWorlds.normalWorlds.contains(e.player.world)) {
            Bukkit.getScheduler().runTask(plugin) {
                e.player.world.getRealPlayersExcept(e.player).forEach { opponent ->
                    GlowAPI.setGlowing(opponent, GlowAPI.Color.RED, e.player)
                    GlowAPI.setGlowing(e.player, GlowAPI.Color.RED, opponent)
                }
            }
        }
    }
    @EventHandler
    fun onTeleportToNormalWorld(e: PlayerTeleportEvent) {
        if (ClassifyWorlds.normalWorlds.contains(e.to.world)) {
            e.to.world.getRealPlayersExcept(e.player).forEach { opponent ->
                GlowAPI.setGlowing(opponent, GlowAPI.Color.RED, e.player)
                GlowAPI.setGlowing(e.player, GlowAPI.Color.RED, opponent)
            }
        }
    }
    @EventHandler
    fun onTeleportToOtherWorld(e: PlayerTeleportEvent) {
        if (!ClassifyWorlds.normalWorlds.contains(e.to.world)) {
            e.from.world.getRealPlayersExcept(e.player).forEach { opponent ->
                GlowAPI.setGlowing(opponent, false, e.player)
                GlowAPI.setGlowing(e.player, false, opponent)
            }
        }
    }
}*/