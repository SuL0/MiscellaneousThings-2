package kr.sul.miscellaneousthings2.mob.spawner

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.*
import org.bukkit.event.world.ChunkUnloadEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object EditMob: Listener {
    private val AKnockbackWorlds = arrayListOf<World>()
    private val BKnockbackWorlds = arrayListOf<World>()
    init {
        Bukkit.getScheduler().runTask(plugin) {
            MobSpawnerConfig.AKnockbackWorldNames.forEach {
                AKnockbackWorlds.add(Bukkit.getWorld(it))
            }
            MobSpawnerConfig.BKnockbackWorldNames.forEach {
                BKnockbackWorlds.add(Bukkit.getWorld(it))
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun stopMobDroppingSomethingOnDeath(e: EntityDeathEvent) {
        if (e.isCancelled) return
        if (e.entity is Monster) {
            e.droppedExp = 0
            e.drops.clear()
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    fun stopMobFromPickingUpDroppedItem(e: EntityPickupItemEvent) {
        if (e.isCancelled) return
        if (e.entity !is Player) {
            e.isCancelled = true
        }
    }

    // 넉백 A (Easy)
    @EventHandler
    fun onMonsterDamaged(e: EntityDamageByEntityEvent) {
        if (e.entity is Monster && e.damager is Player) {
            if (AKnockbackWorlds.contains(e.entity.world)) {
                Bukkit.getScheduler().runTask(plugin) {
                    e.entity.velocity = e.damager.location.direction.multiply(MobSpawnerConfig.AKnockbackStrength)
                }
            }
        }
    }

    // 넉백 B (Hard)
    @EventHandler
    fun onMonsterKnockback(e: EntityKnockbackByEntityEvent) {
        if (e.entity is Monster
            && (e.entity.lastDamageCause.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK
                    || e.entity.lastDamageCause.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            if (BKnockbackWorlds.contains(e.entity.world)) {
                e.isCancelled = true
                e.entity.velocity = e.hitBy.location.direction.multiply(MobSpawnerConfig.BKnockbackStrength)
            }
        }
    }





    // 포션 효과
    @EventHandler
    fun onMobSpawn(e: EntitySpawnEvent) {
        if (e.entity is Zombie) {
            if (e.location.world.name.startsWith("Hard")) {
                (e.entity as Zombie).addPotionEffect(PotionEffect(PotionEffectType.SPEED, 1000000, 1, true, false))
            }
        }
    }

    // 플레이어 바로 앞에 있는 청크가 자꾸 언로드 되려 시도돼서, 좀비가 스폰되자 마자 좀비가 제거되는 문제
//    // NOTE 셜커가 청크 언로드를 Cancel하기에, priority Lowest로 해 두었음
//    @EventHandler(priority = EventPriority.LOWEST)
//    fun onChunkUnload(e: ChunkUnloadEvent) {
//        e.world.entities.filterIsInstance<Zombie>().forEach {
//            Bukkit.broadcastMessage("kill Entity")
//            it.remove()
//        }
//    }
}