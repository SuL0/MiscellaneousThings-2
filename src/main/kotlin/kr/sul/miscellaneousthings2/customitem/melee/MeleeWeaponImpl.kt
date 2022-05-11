package kr.sul.miscellaneousthings2.customitem.melee

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object MeleeWeaponImpl: Listener {
    @EventHandler(priority = EventPriority.LOW)
    fun onAttack(e: EntityDamageByEntityEvent) {
        if (e.isCancelled || e.damager == null || e.damager !is Player) return
        val itemInMainHand = (e.damager as Player).inventory.itemInMainHand
        val find = MeleeWeaponDefinedMgr.findMatching(itemInMainHand) ?: return
        e.damage = find.attackDamage
        // TODO 공격속도 구현
        // TODO 근접무기 크리티컬 구현
        MeleeDurabilityWrapper(find, itemInMainHand as CraftItemStack, e.damager as Player).currentDurability -= 1
    }
}