package kr.sul.miscellaneousthings2.something

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object ArmorWeight: Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        updateArmorWeight(e.player)
    }

    @EventHandler
    fun onEquip(e: PlayerArmorChangeEvent) {
        updateArmorWeight(e.player)
    }

    // 철 풀셋: 0.18F , 다야 풀셋: 0.16F
    private fun updateArmorWeight(p: Player) {
        var walkSpeed = 0.2F
        when (p.inventory.helmet?.type) {
            Material.IRON_HELMET -> walkSpeed -= 0.003F
            Material.DIAMOND_HELMET -> walkSpeed -= 0.006F
            else -> {}
        }
        when (p.inventory.chestplate?.type) {
            Material.IRON_CHESTPLATE -> walkSpeed -= 0.007F
            Material.DIAMOND_CHESTPLATE -> walkSpeed -= 0.014F
            else -> {}
        }
        when (p.inventory.leggings?.type) {
            Material.IRON_LEGGINGS -> walkSpeed -= 0.006F
            Material.DIAMOND_LEGGINGS -> walkSpeed -= 0.014F
            else -> {}
        }
        when (p.inventory.boots?.type) {
            Material.IRON_BOOTS -> walkSpeed -= 0.003F
            Material.DIAMOND_BOOTS -> walkSpeed -= 0.006F
            else -> {}
        }
        p.walkSpeed = walkSpeed
    }
}