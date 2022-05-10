package kr.sul.miscellaneousthings2.customitem.armor

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object ArmorMgr: Listener {
    private val armorPlayerList = hashMapOf<Player, ArmorPlayer>()


    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        armorPlayerList[e.player] = ArmorPlayer(e.player)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        armorPlayerList[e.player]?.destroy()
        armorPlayerList.remove(e.player)
    }

    init {
        Bukkit.getOnlinePlayers().forEach {
            onJoin(PlayerJoinEvent(it, ""))
        }
    }
}