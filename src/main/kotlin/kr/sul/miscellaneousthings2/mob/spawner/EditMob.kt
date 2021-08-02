package kr.sul.miscellaneousthings2.mob.spawner

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.ServerCore.Companion.economy
import kr.sul.servercore.ServerCore.Companion.isEconomyEnabled
import kr.sul.servercore.util.ClassifyWorlds
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.*
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

    // 몹 템 드랍 방지
    @EventHandler(priority = EventPriority.HIGH)
    fun stopMobDroppingSomethingOnDeath(e: EntityDeathEvent) {
        if (e.isCancelled) return
        if (e.entity is Monster) {
            e.droppedExp = 0
            e.drops.clear()
        }
    }
    // 몹 템 먹기 방지
    @EventHandler(priority = EventPriority.HIGH)
    fun stopMobFromPickingUpDroppedItem(e: EntityPickupItemEvent) {
        if (e.isCancelled) return
        if (e.entity !is Player) {
            e.isCancelled = true
        }
    }
    // 몹 불타는 것 방지(특히 태양)
    @EventHandler
    fun preventMobBurning(e: EntityCombustEvent) {
        if (e.entity is Monster) {
            e.isCancelled = true
        }
    }
    // 좀비 압사하려 하면 블럭 -> 좀비 방향으로 밀어내기
    @EventHandler(priority = EventPriority.HIGH)
    fun pushZombieWhenIfSuffos(e: EntityDamageByBlockEvent) {
        if (e.isCancelled) return
        if (e.entity is Zombie && e.cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            e.entity.velocity = e.entity.location.toVector().subtract(e.damager.location.toVector())
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
        if (e.entity is Zombie && ClassifyWorlds.hardWorlds.contains(e.location.world)) {
            (e.entity as Zombie).addPotionEffect(PotionEffect(PotionEffectType.SPEED, 1000000, 1, true, false))
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


    // TODO 테섭용
    // 체력 *3
    @EventHandler(priority = EventPriority.LOW)
    fun onZombieDamage(e: EntityDamageEvent) {
        if (e.isCancelled) return
        if (e.entity is Zombie && ClassifyWorlds.normalWorlds.contains(e.entity.world)) {
            e.damage = e.damage/3.0
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    fun onZombieDeath(e: EntityDeathEvent) {
        if (e.isCancelled) return
        if (!isEconomyEnabled) return
        if (e.entity is Zombie && ClassifyWorlds.normalWorlds.contains(e.entity.world)) {
            e.entity.killer.sendTitle("", "           §a+5,000원", 2, 7, 6)
            economy.depositPlayer(e.entity.killer, 5000.toDouble())
        }
    }
}