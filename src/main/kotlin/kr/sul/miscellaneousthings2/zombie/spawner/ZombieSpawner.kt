package kr.sul.miscellaneousthings2.zombie.spawner

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import kotlin.random.Random

object ZombieSpawner {
    private val random = Random

    private const val SCHEDULER_TERM = 20*5L
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
            arrayListOf("Channel01").forEach {
                worlds.add(Bukkit.getWorld(it))
            }
        }

        // 스케쥴러
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (world in worlds) {
                for (p in world.players) {
                    spawnZombieInNearby(p)
                }
            }
        }, SCHEDULER_TERM, SCHEDULER_TERM)

    }


    // spawnZombie이긴 한데, Husk를 소환할 수도 있음
    private fun spawnZombieInNearby(p: Player) {
        val howManyZombiesToSpawn =
            if (random.nextInt(10)+1 <= 3) random.nextInt(2, 6+1)  // 밀집 스폰(2~6마리) 30%
            else 1  // 개별 스폰 70%

        // 스폰
        val appropirateLoc = AppropriateLocFinder.find(p, SpawnerMobInfo.mobsHabitatBlockTypeList)
            ?: return
        val whatMobToBeSpawned = SpawnerMobInfo.get(appropirateLoc.block.type)

        for (i in 1..howManyZombiesToSpawn) {
            // 여러마리 소환할 땐 위치 1.5칸씩 랜덤하게 옆으로
            whatMobToBeSpawned.spawn(appropirateLoc)
        }
    }
}