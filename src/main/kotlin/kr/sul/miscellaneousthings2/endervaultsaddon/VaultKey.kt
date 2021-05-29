package kr.sul.miscellaneousthings2.endervaultsaddon

import kr.sul.servercore.util.ItemBuilder.loreIB
import kr.sul.servercore.util.ItemBuilder.nameIB
import kr.sul.servercore.util.UniqueIdAPI
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class VaultKey(private val item: ItemStack) {
    WOODEN_KEY(ItemStack(Material.STONE).nameIB("§6창고 열쇠").loreIB("§7 잠긴 창고를 여는 데에 사용할 수 있다."));

    fun getItem(): ItemStack {
        val item = item.clone()
        UniqueIdAPI.carveUniqueID(item)
        return item
    }
    fun isSameKey(itemToCompare: ItemStack): Boolean {
        return (itemToCompare.type == item.type && itemToCompare.durability == item.durability
                && itemToCompare.itemMeta.displayName == item.itemMeta.displayName)
    }

    companion object {
        fun isKeyItem(item: ItemStack): Boolean {
            for (key in VaultKey.values()) {
                if (key.isSameKey(item)) {
                    return true
                }
            }
            return false
        }
    }
}