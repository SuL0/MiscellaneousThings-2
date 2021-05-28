package kr.sul.miscellaneousthings2.zombie.spawner

import de.tr7zw.nbtapi.NBTEntity
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import java.lang.Exception
import java.util.function.Consumer


// habitatBlockType이 다른 몹 Enum 구현체랑 중복되면 안됨
/**
 * @param habitatBlockType p 주변에 랜덤한 위치 찾았더니 해당 블럭이면 해당 몹 소환
 * @param howToSpawn 해당 몹 스폰하는 방법
 */
enum class SpawnerMobInfo(val habitatBlockType: Material, private val howToSpawn: Consumer<Location>) {
    ZOMBIE(Material.GRASS, { spawnLoc ->
        val waitFor = 10*20L
        spawnAnimation(spawnLoc, ItemStack(Material.STONE_AXE), waitFor)          // TODO 스폰 모션
        org.bukkit.Bukkit.getScheduler().runTaskLater(kr.sul.miscellaneousthings2.Main.Companion.plugin, {
            spawnLoc.world.spawnEntity(spawnLoc, EntityType.ZOMBIE)
        }, waitFor)
    }),
    HUSK(Material.SAND, { spawnLoc ->
        val waitFor = 10*20L
        spawnAnimation(spawnLoc, ItemStack(Material.STONE_AXE), waitFor)           // TODO 스폰 모션
        Bukkit.getScheduler().runTaskLater(plugin, {
            spawnLoc.world.spawnEntity(spawnLoc, EntityType.HUSK)
        }, waitFor)
    });

    fun spawn(loc: Location) {
        howToSpawn.accept(loc.clone().add(0.0, 1.0, 0.0))  // 블럭 바로 위로 스폰함으로서 몹이 끼이지 않게 함
    }


    companion object {
        const val ARMORSTAND_TAG = "For Mob Spawn Animation"
        val mobsHabitatBlockTypeList = run {
            val list = arrayListOf<Material>()
            for (habitatBlockType in SpawnerMobInfo.values().map { it.habitatBlockType }) {
                list.add(habitatBlockType)
            }
            return@run list
        }

        fun get(habitatBlockType: Material): SpawnerMobInfo {
            for (mobInfo in SpawnerMobInfo.values()) {
                if (habitatBlockType == mobInfo.habitatBlockType) {
                    return mobInfo
                }
            }
            throw Exception("$habitatBlockType 에 해당하는 SpawnerMobInfo를 찾을 수 없음")
        }



        private fun spawnAnimation(loc: Location, itemForAnimation: ItemStack, waitFor: Long) {
            val armorStand = loc.world.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand
            NBTEntity(armorStand).run { setByte("Invisible", 1); setByte("NoGravity", 1) }  // NBTEntity는 NBTItem과 다르게 item = nbti.item 같은 과정 없어도 알아서 적용됨
            armorStand.customName = ARMORSTAND_TAG
            armorStand.helmet = itemForAnimation

            // waitFor 후에 아머스탠드 삭제
            Bukkit.getScheduler().runTaskLater(plugin, {
                armorStand.remove()
            }, waitFor)
        }
    }
}