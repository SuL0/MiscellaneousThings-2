package kr.sul.miscellaneousthings2.something

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object DamageScouter: Listener {

    private fun sendDamageIndicatorActionbar(p: Player, victim: Player) {
        Bukkit.getScheduler().runTask(plugin) {  // 0틱 시점에는 피가 안 닳은 상태이기 때문
            p.sendActionBar("${victim.name} HP : §c§l${victim.health*250}")
        }
    }


    @EventHandler(priority = EventPriority.HIGH)
    fun onDealDamage(e: EntityDamageByEntityEvent) {
        if (e.isCancelled) return

        val victimPlayer
        = if (e.entity is Player) {
            e.entity as Player
        } else {
            return
        }

        val damagerPlayer
        = if (e.damager is Projectile
                && (e.damager as Projectile).shooter is Player) {
            (e.damager as Projectile).shooter as Player
        } else if (e.damager is TNTPrimed
                && e.damager.hasMetadata("CS_pName")  // 크랙샷으로 발사된 TNTPrimed
                && Bukkit.getPlayerExact(e.damager.getMetadata("CS_pName")[0].asString()) != null) {
            Bukkit.getPlayerExact(e.damager.getMetadata("CS_pName")[0].asString())
        } else if (e.damager is Player) {
            e.damager as Player
        } else {
            return
        }

        sendDamageIndicatorActionbar(damagerPlayer, victimPlayer)
    }
}