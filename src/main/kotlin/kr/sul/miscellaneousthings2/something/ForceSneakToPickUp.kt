package kr.sul.miscellaneousthings2.something

import kr.sul.servercore.util.UptimeBasedOnTick
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

object ForceSneakToPickUp: Listener {
    private val latestMessage = hashMapOf<UUID, Long>()
    private const val INFO_MESSAGE_TERM = 7*20L

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerPickUp(e: EntityPickupItemEvent) {
        if (e.isCancelled) return
        if (e.entity is Player) {
            val p = e.entity as Player
            if (!p.isSneaking) {
                e.isCancelled = true
                if (UptimeBasedOnTick.uptimeBasedOnTick - (latestMessage[p.uniqueId] ?: 0) >= INFO_MESSAGE_TERM) {
                    latestMessage[p.uniqueId] = UptimeBasedOnTick.uptimeBasedOnTick
                    p.sendMessage("§c§lPICKUP: §7아이템을 주우려면 §f§lShift §7를 누르십시오.")
                }
            }
        }
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        latestMessage.remove(e.player.uniqueId)
    }
}