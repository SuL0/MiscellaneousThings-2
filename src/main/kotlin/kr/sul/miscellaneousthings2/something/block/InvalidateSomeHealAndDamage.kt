package kr.sul.miscellaneousthings2.something.block

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent

object InvalidateSomeHealAndDamage: Listener {
    @EventHandler
    fun invalidateWitherPotion(e: EntityDamageEvent) {
        if (e.entity is Player && e.cause == EntityDamageEvent.DamageCause.WITHER) {
            e.isCancelled = true
        }
    }
    @EventHandler
    fun invalidateRegenerationPotion(e: EntityRegainHealthEvent) {
        if (e.regainReason == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun restrainHealthRegenNaturally(e: EntityRegainHealthEvent) {
        if (e.regainReason == EntityRegainHealthEvent.RegainReason.SATIATED) {
            e.isCancelled = true
        }
    }
}