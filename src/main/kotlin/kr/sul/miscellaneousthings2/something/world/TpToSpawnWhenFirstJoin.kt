package kr.sul.miscellaneousthings2.something.world

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object TpToSpawnWhenFirstJoin: Listener {

    // TODO 너무 대충함
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        Bukkit.getScheduler().runTask(plugin) {
            if (e.player.location.world.name == "world") {  // 첫번째 스폰 시 world로 감
                e.player.teleport(Location(Bukkit.getWorld("SPAWN-2"), 1785.5, 50.0, 568.5))
            }
        }
    }
}