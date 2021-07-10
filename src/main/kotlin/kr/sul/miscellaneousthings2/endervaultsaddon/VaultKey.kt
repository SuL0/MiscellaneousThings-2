package kr.sul.miscellaneousthings2.endervaultsaddon

import kr.sul.servercore.nbtapi.NbtItem
import kr.sul.servercore.util.ItemBuilder.loreIB
import kr.sul.servercore.util.ItemBuilder.nameIB
import kr.sul.servercore.util.UniqueIdAPI
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class VaultKey(private val item: ItemStack) {
    WOODEN_KEY(ItemStack(Material.GOLD_NUGGET).nameIB("§6창고 열쇠").loreIB("§7 잠긴 창고를 여는 데에 사용할 수 있다.", 2));

    fun getItem(): ItemStack {
        val item = item.clone()
        UniqueIdAPI.carveUniqueID(item)
        val nbti = NbtItem(item)
        nbti.tag.setString(VAULT_KEY_TYPE, "WOODEN_KEY")
        nbti.applyToOriginal()
        return item
    }
    fun isSameKey(itemToCompare: ItemStack): Boolean {
        val nbti = NbtItem(itemToCompare)
        return (nbti.tag.getString(VAULT_KEY_TYPE) == this.name)  // Key 타입인지 보고, 자신 Enum과 KeyType 비교
    }

    companion object {
        private const val VAULT_KEY_TYPE = "EnderVaultAddon: VaultKeyType"

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