package kr.sul.miscellaneousthings2.something

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

object CombineArmor: Listener {

    // TODO 사운드 추가
    // cursor: 커서에 들린 아이템
    // currentItem: 클릭된 아이템
    @EventHandler
    fun onInvClick(e: InventoryClickEvent) {
        if (e.isRightClick && e.cursor.type == e.currentItem.type
                && isArmor(e.cursor)) {
            if (e.cursor.durability == 0.toShort() || e.currentItem.durability == 0.toShort()) return  // 내구도 풀인 갑옷은 조합 X
            if (e.cursor.amount != 1 || e.currentItem.amount != 1) return
            val maxDurability = e.cursor.type.maxDurability

            val cursor = e.cursor
            e.cursor = ItemStack(Material.AIR)  // 아이템 한 개 제거
            e.isCancelled = true

            e.currentItem.durability = (e.currentItem.durability - (maxDurability-cursor.durability)).toShort()
            if (e.currentItem.durability < 0) {
                e.currentItem.durability = 0
            }
        }
    }

    private fun isArmor(itemStack: ItemStack): Boolean {
        val typeNameString: String = itemStack.type.name
        return (typeNameString.endsWith("_HELMET")
                || typeNameString.endsWith("_CHESTPLATE")
                || typeNameString.endsWith("_LEGGINGS")
                || typeNameString.endsWith("_BOOTS"))
    }
}