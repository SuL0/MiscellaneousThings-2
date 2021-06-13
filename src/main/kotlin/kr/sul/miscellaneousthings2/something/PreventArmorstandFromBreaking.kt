package kr.sul.miscellaneousthings2.something

import org.bukkit.GameMode
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent

object PreventArmorstandFromBreaking: Listener {

    // 투사체, LeftClick 으로부터의 파괴를 방지
    @EventHandler(priority = EventPriority.LOW)
    fun preventBreaking(e: EntityDamageByEntityEvent) {
        if (e.entity is ArmorStand) {
            if (e.damager is Player && e.damager.isOp
                && (e.damager as Player).gameMode == GameMode.CREATIVE) {
                return
            }

            // 이거 안 하면, 투사체가 반대 방향으로 튕겨서 계속 진행함
            if (e.damager is Projectile) {
                e.damager.remove()
            }
            e.isCancelled = true
        }
    }

    // RightClick로 템 빼앗는 것을 감지
    // NOTE 왜 PlayerInteractEntityEvent 는 우클릭 감지를 못 하는가
    @EventHandler(priority = EventPriority.HIGHEST)
    fun preventStealingItem(e: PlayerInteractAtEntityEvent) {
        if (e.rightClicked is ArmorStand) {
            if (e.player.isOp && e.player.gameMode == GameMode.CREATIVE) {
                return
            }
            e.isCancelled = true
        }
    }
}