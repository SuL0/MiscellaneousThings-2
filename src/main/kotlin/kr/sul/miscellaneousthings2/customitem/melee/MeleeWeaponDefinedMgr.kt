package kr.sul.miscellaneousthings2.customitem.melee

import kr.sul.miscellaneousthings2.customitem.enums.CustomItemDefinedMgr
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack

object MeleeWeaponDefinedMgr
        : CustomItemDefinedMgr<MeleeWeaponDefined>("Melee", MeleeWeaponDefined.values()) {
    override fun makeNewItemStack(value: MeleeWeaponDefined): CraftItemStack {
        val item = super.makeNewItemStack(value)
        item.lore = value.loreFunc.invoke(value.maxDurability, value.maxDurability)
        return item
    }
}