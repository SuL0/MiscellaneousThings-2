package kr.sul.miscellaneousthings2.zombie.spawner

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

object EditMob: Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun stopMobFromPickingUpDroppedItem(e: EntityPickupItemEvent) {
        if (e.isCancelled) return
        if (e.entity !is Player) {
            e.isCancelled = true
        }
    }
}