package kr.sul.miscellaneousthings2.customitem.melee

import kr.sul.miscellaneousthings2.customitem.util.DurabilityItemWrapper
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.entity.Player

class MeleeDurabilityWrapper(private val enumFound: MeleeWeaponDefined, item: CraftItemStack, p: Player? = null)
        : DurabilityItemWrapper(item, p) {
    override var currentDurability: Int
        get() = super.currentDurability
        set(value) {
            item.lore = enumFound.loreFunc.invoke(currentDurability-1, enumFound.maxDurability)
            super.currentDurability = value
            // 아이템이 파괴됐을 수 있음
        }
}