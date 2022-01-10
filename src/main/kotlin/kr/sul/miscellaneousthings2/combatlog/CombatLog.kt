package kr.sul.miscellaneousthings2.combatlog

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object CombatLog: Listener {
    private val combatStateMap = hashMapOf<Player, CombatState>()

    @EventHandler(priority = EventPriority.HIGH)
    fun onStartCombat(e: EntityDamageByEntityEvent) {
        if (!(e.damager is Player && e.entity is Player) || e.isCancelled) return
        val damager = e.damager as Player
        val victim = e.entity as Player
        if (!combatStateMap.contains(e.damager)) {
            combatStateMap[damager] = CombatState(damager)
        }
        if (!combatStateMap.contains(victim)) {
            combatStateMap[victim] = CombatState(victim)
        }
    }

    fun onCombatStatusDestroyed(combatState: CombatState) {
        combatStateMap.remove(combatState.p)
    }
}