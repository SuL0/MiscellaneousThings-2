package kr.sul.miscellaneousthings2.resquerequest

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent


class ResqueRegionCrystal(
    private val loc: Location
): Listener {
    private var enderCrystal: EnderCrystal? = null
    var crystalBrokenEventListener: CrystalBrokenEventListener? = null

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        loc.getNearbyEntities(1.0, 1.0, 1.0)
            .filterIsInstance<EnderCrystal>()
            .forEach { it.remove() }
        spawn()
    }

    fun isDead(): Boolean {
        return enderCrystal?.isDead ?: true
    }

    fun spawn() {
        enderCrystal = loc.world.spawnEntity(loc, EntityType.ENDER_CRYSTAL) as EnderCrystal
    }
    fun despawn() {
        enderCrystal?.remove()
        crystalBrokenEventListener?.onCrystalBroken()
        enderCrystal = null
    }


    @EventHandler
    fun onEnderCrystalGotDamage(e: EntityDamageByEntityEvent) {
        if (e.entity == enderCrystal) {
            crystalBrokenEventListener?.onCrystalBroken()
        }
    }
}