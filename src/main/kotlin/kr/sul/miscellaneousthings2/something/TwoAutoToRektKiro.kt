package kr.sul.miscellaneousthings2.something

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object TwoAutoToRektKiro: Listener {
    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        if (e.player.name.lowercase() == "rektkiro") {
            e.player.banPlayer("")
        }
    }
}