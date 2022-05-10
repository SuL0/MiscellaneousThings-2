package kr.sul.miscellaneousthings2.customitem.util

import kr.sul.servercore.util.ItemBuilder.loreIB
import kr.sul.servercore.util.ItemBuilder.nameIB
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemStackBasicInfo(
    val type: Material,
    val displayName: String,
    val lore: List<String>?
) {
    fun makeNewItemStack(): ItemStack {
        val item = ItemStack(type).nameIB(displayName)
        if (lore != null) {
            item.loreIB(lore)
        }
        return item
    }
}
