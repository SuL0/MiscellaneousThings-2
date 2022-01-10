package kr.sul.miscellaneousthings2.combatlog

import com.shampaggon.crackshot2.CSDirector
import com.shampaggon.crackshot2.CSMinion
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class DamageTakenReport(private val p: Player): Listener {
    private var isReporting = false
    var numOfHeadShot = 0
    var numOfNormalShot = 0
    var numOfExplosion = 0
    var numOfEtcAttack = 0

    var totalDamageOfHeadShot = 0.0
    var totalDamageOfNormalShot = 0.0
    var totalDamageOfExplosion = 0.0
    var totalDamageOfEtc = 0.0

    @EventHandler(priority = EventPriority.HIGH)
    fun onDamage(e: EntityDamageEvent) {
        if (e.entity != p || e.isCancelled) return
        if (e is EntityDamageByEntityEvent) {
            if (e.damager is Projectile && (e.damager as Projectile).shooter is Player && e.damager.hasMetadata("projParentNode")) {
                val parentNode = e.damager.getMetadata("projParentNode")[0].asString()
                val projSpeed = CSDirector.getInstance().getProjectileSpeed((e.damager as Projectile).shooter as Player, parentNode)
                if (CSMinion.getInstance().isHesh(e.damager as Projectile, p, projSpeed)) {
                    numOfHeadShot++
                    totalDamageOfHeadShot += e.finalDamage
                } else {
                    numOfNormalShot++
                    totalDamageOfNormalShot += e.finalDamage
                }
            } else if (e.cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || e.cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                numOfExplosion++
                totalDamageOfExplosion += e.finalDamage
            } else {
                numOfEtcAttack++
                totalDamageOfEtc += e.finalDamage
            }
        } else {
            numOfEtcAttack++
            totalDamageOfEtc += e.finalDamage
        }
    }

    fun startToReport(): DamageTakenReport {
        isReporting = true
        Bukkit.getPluginManager().registerEvents(this, plugin)
        return this
    }
    fun stopToReport() {
        isReporting = false
        HandlerList.unregisterAll(this)
    }
}