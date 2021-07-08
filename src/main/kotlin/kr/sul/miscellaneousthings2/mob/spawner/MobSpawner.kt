package kr.sul.miscellaneousthings2.mob.spawner

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import kotlin.random.Random

object MobSpawner {
    private val random = Random

    private val SCHEDULER_TERM = 20 * MobSpawnerConfig.mobSpawningTerm.toLong()
    private val worlds = arrayListOf<World>()

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

        // 대상 월드 등록
        Bukkit.getScheduler().runTask(plugin) {
            MobSpawnerConfig.worldNames.forEach {
                worlds.add(Bukkit.getWorld(it))
            }
        }

        // 스케쥴러
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (world in worlds) {
                for (p in world.players) {
                    spawnMobInNearby(p)
                }
            }
        }, SCHEDULER_TERM, SCHEDULER_TERM)

    }


    // Zombie 또는 Husk
    private fun spawnMobInNearby(p: Player) {
        val range = MobSpawnerConfig.numRangeOfSpawningMobDensely
        val howManyMobsToSpawn =
            if (random.nextInt(0, 100+1) <= MobSpawnerConfig.chanceOfSpawningMobDensely) {
                random.nextInt(range.min, range.max+1)
            } else {
                1
            }

        // 스폰
        val appropirateLoc = AppropriateLocFinder.find(p, SpawnerMobInfo.mobsHabitatBlockTypeList)
            ?: return
        val whatMobToBeSpawned = SpawnerMobInfo.get(appropirateLoc.block.type)

        for (i in 1..howManyMobsToSpawn) {
            // 여러마리 소환할 땐 위치 1.5칸씩 랜덤하게 옆으로
            whatMobToBeSpawned.spawn(appropirateLoc)
        }
    }
}