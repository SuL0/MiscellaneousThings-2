package kr.sul.miscellaneousthings2.customitem.itemlistgui.button

import kr.sul.servercore.util.ItemBuilder.nameIB
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class InnerPageButton(
    override val whatSlotToAllocate: Int,
    override val buttonItem: ItemStack
): Button {

    PREVIOUS_BUTTON(
        45,
        ItemStack(Material.WOOL, 1, 1).nameIB("§6§l이전 페이지로")
    ),
    NEXT_BUTTON(
        53,
        ItemStack(Material.WOOL, 1, 5).nameIB("§2§l다음 페이지로")
    );
}