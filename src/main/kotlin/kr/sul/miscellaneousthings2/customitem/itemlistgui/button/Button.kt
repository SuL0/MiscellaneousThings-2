package kr.sul.miscellaneousthings2.customitem.itemlistgui.button

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

interface Button {
    val whatSlotToAllocate: Int
    val buttonItem: ItemStack

    fun place(inv: Inventory) {
        inv.setItem(whatSlotToAllocate, buttonItem)
    }
}