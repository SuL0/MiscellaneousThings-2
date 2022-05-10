package kr.sul.miscellaneousthings2.customitem.util

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

object CustomItemUtil {
    fun parseToRealItemStack(item: ItemStack): CraftItemStack {
        return CraftItemStack.asCraftMirror(CraftItemStack.asNMSCopy(item))
    }
}