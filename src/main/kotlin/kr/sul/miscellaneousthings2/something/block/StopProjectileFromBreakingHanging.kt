package kr.sul.miscellaneousthings2.something.block

import org.bukkit.GameMode
import org.bukkit.entity.Hanging
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.hanging.HangingBreakByEntityEvent


object StopProjectileFromBreakingHanging: Listener {
    // Hanging(Paint, ItemFrame) 을 Op인 플레이어가 GM 1로 손으로 부술 때만 허용


    // 단순히 액자만 부숴질 때 (아이템 없이 액자만 있을 때)
    // 이건, 내가 총을 쏴서 맞춰도 e.remover 이 플레이어로 뜸
    @EventHandler(priority = EventPriority.HIGH)
    fun onHangingBreakByEntity(e: HangingBreakByEntityEvent) {
        if (e.remover is Player && (e.remover as Player).gameMode == GameMode.CREATIVE && e.remover.isOp) return

        e.isCancelled = true
    }


    // 아이템 들은 액자를, 어떤 사유에 의해서 아이템이 빠지려 할 때
    // 이건, 내가 총을 쏴서 맞추면 e.damager 이 투사체로 뜸
    @EventHandler
    fun onFrameDamage(e: EntityDamageByEntityEvent) {
        if (e.entity is Hanging) {
            if (e.damager is Player && (e.damager as Player).gameMode == GameMode.CREATIVE && e.damager.isOp) return

            e.isCancelled = true
        }
    }
}