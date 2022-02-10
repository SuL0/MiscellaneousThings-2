package kr.sul.miscellaneousthings2.something.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object TwoAuthToRektKiro: Listener {
    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        if (e.player.name.lowercase() == "rektkiro") {
            e.player.banPlayer("")
        }
    }
}