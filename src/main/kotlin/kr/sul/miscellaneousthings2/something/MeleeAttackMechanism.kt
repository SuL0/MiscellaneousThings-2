package kr.sul.miscellaneousthings2.something

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.EntityTempDataMap
import kr.sul.servercore.util.ItemBuilder.nameIB
import kr.sul.servercore.util.UptimeBasedOnTick
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/*
object MeleeAttackMechanism: Listener {
    private const val ATTACK_COOLDOWN = 15
    private val attackCooldownTimestampMap = EntityTempDataMap.create<AttackCooldown>(plugin)  // 공격 못하게 막는 시간
    private val COOLDOWN_ITEM = ItemStack(Material.STAINED_GLASS_PANE, 1, 5)


    // PlayerInteractEvent에서 왼클이 블럭을 향하지 않으면, world에 변화를 주지 않은것으로 판단해서 e.isCancelled를 true로 만들어버리는 듯?
    @EventHandler
    fun onSwingMeleeWeapon(e: PlayerInteractEvent) {
        if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) return
        val p = e.player
        val originalItem = p.inventory.itemInMainHand
        if (p.isInAttackCooldown(originalItem.type)) return


        p.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, ATTACK_COOLDOWN, 10, true, false), false)
//        Bukkit.getScheduler().runTaskLater(plugin, {
//            p.inventory.itemInMainHand = COOLDOWN_ITEM.nameIB("§fWooden Sword")  // TODO 이름 변경
//        }, 2L)
//        Bukkit.getScheduler().runTaskLater(plugin, {
//            p.inventory.itemInMainHand = originalItem
//        }, ATTACK_COOLDOWN.toLong()-1)

        p.setAttackCooldown(originalItem.type, ATTACK_COOLDOWN)
        p.setCooldown(COOLDOWN_ITEM.type, ATTACK_COOLDOWN)
    }


    // 쿨타임 때 공격 시도 시 공격 취소
    @EventHandler(priority = EventPriority.HIGH)
    fun onAttack(e: EntityDamageByEntityEvent) {
        if (e.isCancelled) return
        if (e.damager is Player && (e.damager as Player).isInAttackCooldown((e.damager as Player).inventory.itemInMainHand.type)) {
            e.isCancelled = true
        }
    }


    private fun Player.setAttackCooldown(material: Material, cooldown: Int) {
        attackCooldownTimestampMap[this] = AttackCooldown(material, UptimeBasedOnTick.uptimeBasedOnTick + cooldown)
    }
    private fun Player.isInAttackCooldown(material: Material): Boolean {
        if (this.inventory.itemInMainHand.type == COOLDOWN_ITEM.type) return true  // 쿨타임 아이템(투명템) 들고있을 때
        val tempValue = attackCooldownTimestampMap[this]
            ?: return false
        return (tempValue.material == material && UptimeBasedOnTick.uptimeBasedOnTick <= tempValue.cooldownTimestamp)
    }

    data class AttackCooldown(val material: Material, val cooldownTimestamp: Long)
}*/