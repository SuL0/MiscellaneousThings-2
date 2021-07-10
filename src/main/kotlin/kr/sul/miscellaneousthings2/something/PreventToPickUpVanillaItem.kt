package kr.sul.miscellaneousthings2.something

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.file.simplylog.LogLevel
import kr.sul.servercore.file.simplylog.SimplyLog
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

object PreventToPickUpVanillaItem: Listener {

    // 일단 혹시 몰라 감지만 하는 용도이므로, priority HIGH
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPickUpVanillaItem(e: EntityPickupItemEvent) {
        if (e.isCancelled) return
        if (e.entity is Player && !e.entity.isOp && (e.entity as Player).gameMode == GameMode.SURVIVAL) {
            if (!e.item.itemStack.itemMeta.hasDisplayName()) {
                val loc = e.item.location.toBlockLocation()
                SimplyLog.log(LogLevel.ERROR_LOW, plugin, "[바닐라 아이템 획득] ${e.entity.name} 이 ${e.item.itemStack.type} 를 획득함 (loc: ${loc.x}, ${loc.y}, ${loc.z})")
            }
        }
    }
}