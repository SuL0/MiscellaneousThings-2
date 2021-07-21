package kr.sul.miscellaneousthings2.mob.spawner

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.ClassifyWorlds
import kr.sul.servercore.util.ItemBuilder.durabilityIB
import kr.sul.servercore.util.ItemBuilder.unbreakableIB
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Zombie
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.absoluteValue

object NormalZombie: Listener {
    private const val MIN_MOVE_SPEED = 0.001
    private val stopMotionItem = ItemStack(Material.FLINT_AND_STEEL).durabilityIB(16).unbreakableIB(true)
    private val movingMotionItem = ItemStack(Material.FLINT_AND_STEEL).durabilityIB(17).unbreakableIB(true)
    private val invisibledZombie = arrayListOf<Int>()

    // TODO Async 사용해서 최적화 해보는 것도 고려
    init {
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (normalWorld in ClassifyWorlds.normalWorlds) {
                for (zombie in normalWorld.entities.filterIsInstance<Zombie>()) {
                    if (!zombie.chunk.isLoaded) continue
                    if (!invisibledZombie.contains(zombie.entityId)) {  // 단 한번만 투명포션 적용
                        zombie.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1, false, false))
                        invisibledZombie.add(zombie.entityId)
                    }
                    // 청크가 살아있어 움직이고 있는 정상좀비
                    if (zombie.velocity.x.absoluteValue >= MIN_MOVE_SPEED && zombie.velocity.z.absoluteValue >= MIN_MOVE_SPEED) {
                        makeZombieLookLikeMoving(zombie)
                    } else {
                        makeZombieLookLikeStopped(zombie)
                    }
                }
            }
        }, 1L, 1L)
    }


    private fun makeZombieLookLikeNormalZombie(zmb: Zombie) {
        zmb.equipment.helmet = null
    }
    private fun makeZombieLookLikeStopped(zmb: Zombie) {
        zmb.equipment.helmet = stopMotionItem
    }
    private fun makeZombieLookLikeMoving(zmb: Zombie) {
        zmb.equipment.helmet = movingMotionItem
    }
}