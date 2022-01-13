package kr.sul.miscellaneousthings2.mob.spawner

import de.tr7zw.nbtapi.NBTEntity
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.ItemBuilder.durabilityIB
import kr.sul.servercore.util.ItemBuilder.unbreakableIB
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import java.lang.Exception
import java.util.function.Consumer


/**
 * @param habitatBlockType p 주변에 랜덤한 위치 찾았더니 해당 블럭이면 해당 몹 소환
 * @param howToSpawn 해당 몹 스폰하는 방법
 */
enum class SpawnerMobInfo(private val howToSpawn: Consumer<Location>) {
    ZOMBIE( { spawnLoc ->
        val waitFor = 2*20L
        spawnAnimation(spawnLoc, ItemStack(Material.FLINT_AND_STEEL).durabilityIB(30).unbreakableIB(true), waitFor)
        // 몹 소환
        Bukkit.getScheduler().runTaskLater(plugin, {
            val mob = spawnLoc.world.spawnEntity(spawnLoc, EntityType.ZOMBIE) as org.bukkit.entity.Zombie
            mob.world.spawnParticle(org.bukkit.Particle.CLOUD, mob.location,
                2, 0.0, 0.0, 0.0, 0.1)
            unarmorLivingEntity(mob)
            mob.isBaby = false
        }, waitFor)
    });

    fun spawn(loc: Location) {
        howToSpawn.accept(loc.clone().add(0.0, 1.0, 0.0))  // 블럭 바로 위로 스폰함으로서 몹이 끼이지 않게 함
    }






    // 애니메이션용 아머스탠드는 패킷으로 하고, 스레드는 async가 맞긴 할텐데.. 뭐 이것도 문제는 없으므로
    companion object SpawnAnimationUtil {
        const val ARMORSTAND_TAG = "For Mob Spawn Animation"

        /**
         * itemForAnimation 아이템 내구도가 2틱마다 줄어들면서 Animation
         */
        private fun spawnAnimation(loc: Location, itemForAnimation: ItemStack, waitFor: Long) {
            val armorStand = loc.world.spawnEntity(loc.clone().add(0.0, -0.5, 0.0), EntityType.ARMOR_STAND) as ArmorStand
            NBTEntity(armorStand).run { setByte("Invisible", 1); setByte("NoGravity", 1) }  // NBTEntity는 NBTItem과 다르게 item = nbti.item 같은 과정 없어도 알아서 적용됨
            armorStand.customName = ARMORSTAND_TAG
            armorStand.helmet = itemForAnimation

            // 2틱마다 내구도 1씩 까기
            val task = Bukkit.getScheduler().runTaskTimer(plugin, {
                itemForAnimation.durability = (itemForAnimation.durability + 1).toShort()
                armorStand.helmet = itemForAnimation
            }, 2L, 2L)

            // waitFor 후에 아머스탠드 삭제
            Bukkit.getScheduler().runTaskLater(plugin, {
                task.cancel()
                armorStand.remove()
            }, waitFor)
        }

        private fun unarmorLivingEntity(ent: LivingEntity) {
            ent.equipment.helmet = null
            ent.equipment.chestplate = null
            ent.equipment.leggings = null
            ent.equipment.boots = null
            ent.equipment.itemInMainHand = null
            ent.equipment.itemInOffHand = null
        }
    }
}