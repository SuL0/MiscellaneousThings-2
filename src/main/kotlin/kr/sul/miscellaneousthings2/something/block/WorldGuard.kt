package kr.sul.miscellaneousthings2.something.block

import org.bukkit.GameMode
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Hanging
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent


object WorldGuard: Listener {

    // | 블럭 관련
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onBlockPlace(e: BlockPlaceEvent) {
        if (e.player.isOp && e.player.gameMode == GameMode.CREATIVE) return
        e.isCancelled = true
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onBlockBreak(e: BlockBreakEvent) {
        if (e.player.isOp && e.player.gameMode == GameMode.CREATIVE) return
        e.isCancelled = true
    }



    // | 액자 관련
    // Hanging(Paint, ItemFrame) 을 Op인 플레이어가 GM 1로 손으로 부술 때만 허용

    // 단순히 액자만 어떤 사유로든 부숴질 때 (아이템 없이 액자만 있을 때)
    // 이건, 내가 총을 쏴서 맞춰도 e.remover 이 플레이어로 뜸
    @EventHandler(priority = EventPriority.HIGH)
    fun onHangingBreakByEntity(e: HangingBreakByEntityEvent) {
        if (e.remover is Player && (e.remover as Player).gameMode == GameMode.CREATIVE && e.remover.isOp) return

        e.isCancelled = true
    }
    // 아이템이 들어있는 액자가, 어떤 사유에 의해서 들어있던 아이템이 밖으로 빠지려 할 때
    // 이건, 내가 총을 쏴서 맞추면 e.damager 이 투사체로 뜸
    @EventHandler
    fun onFrameDamage(e: EntityDamageByEntityEvent) {
        if (e.entity is Hanging) {
            if (e.damager is Player && (e.damager as Player).gameMode == GameMode.CREATIVE && e.damager.isOp) return

            e.isCancelled = true
        }
    }


    // | 아머스탠드 관련

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
    // 의문) 왜 PlayerInteractEntityEvent 는 우클릭 감지를 못 하는가?
    // PlayerInteractAtEntityEvent 가 PlayerInteractEntityEvent를 상속했으니 당연히 돼야 하지 않나?
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


