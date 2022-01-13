package kr.sul.miscellaneousthings2.mob.spawner

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.ClassifyWorlds
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import kotlin.random.Random

object MobSpawner {
    private val random = Random

    private const val SCHEDULER_TERM = 20 * 10L
    private val worlds
        get() = ClassifyWorlds.beachTownWorlds
    private const val chanceOfSpawningMobDensely = 20
    private val numRangeOfSpawningMobDensely = NumRange(2, 3)
    private val spawnBlockType = arrayListOf<Material>().run {
        add(Material.CONCRETE)
        add(Material.SAND)
        add(Material.SANDSTONE)
        add(Material.GRASS)
        add(Material.DIRT)
        add(Material.MAGENTA_GLAZED_TERRACOTTA)
        add(Material.STONE_SLAB2)
        add(Material.DOUBLE_STONE_SLAB2)
        this
    }

    init {
        // 서버가 강제종료 됐거나, 오류로 인해서 남은 애니메이션 아머스탠드를 모두 제거
        Bukkit.getScheduler().runTask(plugin) {
            Bukkit.getWorlds().forEach { world ->
                world.entities.filterIsInstance<ArmorStand>().forEach { armorStand ->
                    if (armorStand.customName == SpawnerMobInfo.ARMORSTAND_TAG) {
                        armorStand.remove()
                    }
                }
            }
        }

        // 스케쥴러
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (world in worlds) {
                // world.players에 시티즌 포함되도 상관없겠지?
                for (p in world.players) {
                    spawnMobInNearby(p)
                }
            }
        }, SCHEDULER_TERM, SCHEDULER_TERM)

    }


    // Zombie
    private fun spawnMobInNearby(p: Player) {
        val range = numRangeOfSpawningMobDensely // 밀집 스폰 마리수 범위
        val howManyMobsToSpawn =
            if (random.nextInt(0, 100+1) <= chanceOfSpawningMobDensely) {
                random.nextInt(range.min, range.max+1)
            } else {
                1
            }

        // TODO [임시방편] 주변에 몹이 너무 많으면 일단 몹 스폰 중지
        if (p.getNearbyEntities(30.0, 30.0, 30.0)
                .filterIsInstance<Monster>().size >= 10) {
            return
        }

        val appropirateLoc = AppropriateLocFinder.find(p, spawnBlockType)
            ?: return
        val whatMobToBeSpawned = SpawnerMobInfo.ZOMBIE   // 몹 종류 고르는 곳

        // 밀집 스폰
        for (i in 1..howManyMobsToSpawn) {
            // 여러마리 소환할 땐 위치 1.5칸씩 랜덤하게 옆으로
            whatMobToBeSpawned.spawn(appropirateLoc)
        }
    }

    data class NumRange(val min: Int, val max: Int)
}