package kr.sul.miscellaneousthings2.customitem.util

import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.entity.Player

// TODO 아이템이 겹쳐진 상태라면? > 사용을 막아야 함
open class DurabilityItemWrapper(val item: CraftItemStack, val p: Player? = null) {
    companion object {
        private const val CURRENT_DURABILITY_TAG = "Misc-CurrentDurability"
        private const val MAX_DURABILITY_TAG = "Misc-MaxDurability"
    }
    open var currentDurability: Int
        get() {
            return item.handle.tagOrDefault.getInt(CURRENT_DURABILITY_TAG)
        }
        set(value) {
            if (value <= 0) {
                // 아이템 부셔지는 효과
                if (p != null) {
                    p.spawnParticle(Particle.ITEM_CRACK, p.eyeLocation.add(p.location.direction.multiply(0.7)), 2, 0.3, 0.2, 0.3, 0.0, item)
                    p.playSound(p.eyeLocation, Sound.ENTITY_ITEM_BREAK, 1F, 1F)
                }
                item.amount -= 1
                return
            }
            item.handle.tagOrDefault.setInt(CURRENT_DURABILITY_TAG, value)
        }
    var maxDurability: Int
        get() {
            return item.handle.tagOrDefault.getInt(MAX_DURABILITY_TAG)
        }
        set(value) {
            item.handle.tagOrDefault.setInt(MAX_DURABILITY_TAG, value)
        }

    fun editMaxDurability(maxDurability: Int) {
        if (this.maxDurability < maxDurability) {
            val difference = maxDurability - this.maxDurability
            this.maxDurability = maxDurability
            this.currentDurability += difference
        } else {
            if (this.currentDurability > maxDurability) {
                this.currentDurability = maxDurability
            }
            this.maxDurability = maxDurability
        }
    }

    fun initMaxDurability(maxDurability: Int) {
        this.maxDurability = maxDurability
        this.currentDurability = maxDurability
    }
}