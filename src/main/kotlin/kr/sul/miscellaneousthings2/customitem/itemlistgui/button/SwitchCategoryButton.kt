package kr.sul.miscellaneousthings2.customitem.itemlistgui.button

import kr.sul.miscellaneousthings2.customitem.itemlistgui.SamplePage
import kr.sul.servercore.util.ItemBuilder.nameIB
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class SwitchCategoryButton(
    override val whatSlotToAllocate: Int,
    val matchingSamplePage: SamplePage
) : Button {
    override val buttonItem = ItemStack(Material.CONCRETE, 1, whatSlotToAllocate.toShort())
            .nameIB("§6§l> §f§l${matchingSamplePage.name}")
}