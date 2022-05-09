package kr.sul.miscellaneousthings2.something.world.spawn

import org.bukkit.Location
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSlime
import org.bukkit.entity.Entity
import org.bukkit.entity.Slime
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class HitboxEntity(private val loc: Location, var entity: Entity) {
    fun spawnNewOneIfItIsRemoved(): Boolean {
        if (entity.isDead) {
            entity = spawnHitBoxEntity(loc)
            return true
        }
        return false
    }


    companion object {
        fun spawnHitBox(loc: Location): HitboxEntity {
            return HitboxEntity(loc, spawnHitBoxEntity(loc))
        }

        // Slime으로 하드코딩됨
        private fun spawnHitBoxEntity(loc: Location): Entity {
            val entity = loc.world.spawnEntity(loc, SpawnWorldFeatures.HITBOX_ENT_TYPE)
            val nmsEntity = (entity as CraftSlime).handle
            nmsEntity.isNoAI = true
            nmsEntity.setInvulnerable(true)
            nmsEntity.isSilent = true
            nmsEntity.isNoGravity = true
            nmsEntity.customName = SpawnWorldFeatures.HITBOX_ENT_NAME
            nmsEntity.customNameVisible = false
            if (entity is Slime) {
                entity.size = 2
            }
            entity.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 100, true), true)
            return entity
        }
    }
}