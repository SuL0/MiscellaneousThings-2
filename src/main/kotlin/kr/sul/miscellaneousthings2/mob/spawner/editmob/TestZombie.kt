package kr.sul.miscellaneousthings2.mob.spawner.editmob

import com.comphenix.protocol.PacketType
import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.EntityTempDataMap
import kr.sul.servercore.util.ProtocolManager.pm
import kr.sul.servercore.util.UptimeBasedOnTick
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
바닐라기준 작성

유형 : 좀비
체력 : 20 ( 2당 하트 1 )
데미지 : 5 ( 2당 하트 1 )
이동속도 : 플레이어 뒤로 가는 속도보다 빠름
플레이어 앞으로 "뛰지않고 달리는 속도" 보단 느림

공격유형 : 다단히트 3연타 ( 5÷3 = x ) 1타당 x만큼 0.1초당 1타를 때리는 형식

공격사거리 : 1칸 ( 유튜브로 동영상 기재 필요함 )

반동 타입 : 이전에 만들어둔거 사용
 */
object TestZombie: Listener {
    private const val ZOMBIE_DAMAGE = 5/3.0
    private val attackCooldownTimestampMap = EntityTempDataMap.create<Long>(plugin)  // 공격 못하게 막는 시간
    private const val ATTACK_COOLDOWN_TERM = 20L

    @EventHandler
    fun onZombieSpawn(e: EntitySpawnEvent) {
        if (e.entity is Zombie) {
            val zombie = e.entity as Zombie
            zombie.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 999999, 2, true, false))
            zombie.maxHealth = 20.toDouble()
            zombie.health = zombie.maxHealth
        }
    }


    // p.damage(damage, entity) 로 attacker을 넣어줬을 때 데미지가 들어가질 않음
    @EventHandler
    fun onAttack(e: EntityDamageByEntityEvent) {
        if (e.damager is Zombie && e.entity is Player && e.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
//            if (isInAttackCooldown(e.damager as Zombie)) {
//                e.isCancelled = true
//                return
//            }
//            setAttackCooldown(e.damager as Zombie)
//            e.isCancelled = true
            val p = e.entity as Player

            val packet = pm.createPacket(PacketType.Play.Server.ANIMATION)
            packet.integers.write(0, e.damager.entityId)
            packet.integers.write(1, 0)
            e.damage = ZOMBIE_DAMAGE
//            p.damage(ZOMBIE_DAMAGE)

            Bukkit.getScheduler().runTaskLater(plugin, {
                pm.sendServerPacket(p, packet)
                setTempVulnerability(p)
                p.damage(ZOMBIE_DAMAGE)

                Bukkit.getScheduler().runTaskLater(plugin, {
                    pm.sendServerPacket(p, packet)
                    setTempVulnerability(p)
                    p.damage(ZOMBIE_DAMAGE)
                }, 2L)
            }, 2L)
        }
    }


    @EventHandler
    fun onKnockBack(e: EntityKnockbackByEntityEvent) {
        if (e.entity is Zombie) {
            e.isCancelled = true
        }
    }
    @EventHandler
    fun onMonsterDamagedByPlayer(e: EntityDamageEvent) {
        if (e.entity is Zombie) {
            (e.entity as Monster).addPotionEffect(PotionEffect(PotionEffectType.SLOW, 5, 3, true, false))
        }
    }


    private fun setTempVulnerability(ent: LivingEntity) {
        val maxNoDamageTicks = ent.maximumNoDamageTicks
        ent.maximumNoDamageTicks = 0
        ent.noDamageTicks = 0
        if (!ent.hasMetadata("[CS] NHC")) {
            ent.setMetadata("[CS] NHC", FixedMetadataValue(plugin, true))
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
                ent.maximumNoDamageTicks = maxNoDamageTicks
                ent.noDamageTicks = 0
                ent.removeMetadata("[CS] NHC", plugin)
            }, 2L)
        }
    }

    private fun setAttackCooldown(mob: Zombie) {
        attackCooldownTimestampMap[mob] = UptimeBasedOnTick.uptimeBasedOnTick
    }
    private fun isInAttackCooldown(mob: Zombie) : Boolean {
        if (!attackCooldownTimestampMap.containsKey(mob)) {
            return false
        }
        return (UptimeBasedOnTick.uptimeBasedOnTick - attackCooldownTimestampMap[mob]!! < ATTACK_COOLDOWN_TERM)
    }

}