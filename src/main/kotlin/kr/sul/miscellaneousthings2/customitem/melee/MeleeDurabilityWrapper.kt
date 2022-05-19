package kr.sul.miscellaneousthings2.customitem.melee

import kr.sul.miscellaneousthings2.customitem.util.DurabilityItemWrapper
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.entity.Player

class MeleeDurabilityWrapper(private val enumFound: MeleeWeaponDefined, item: CraftItemStack, p: Player? = null)
        : DurabilityItemWrapper(item, p) {

    override var currentDurability: Int
        get() = super.currentDurability
        set(value) {
            super.currentDurability = value
            // 아이템이 부숴지지 않았을 경우
            if (value > 0) {
                item.lore = enumFound.loreFunc.invoke(currentDurability-1, enumFound.maxDurability)
            }
        }
}