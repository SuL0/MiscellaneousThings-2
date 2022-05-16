package kr.sul.miscellaneousthings2.customitem.forenum

import kr.sul.miscellaneousthings2.customitem.util.DurabilityItemWrapper
import kr.sul.servercore.util.UniqueIdAPI
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

open class CustomItemDefinedMgr<T: NormalItem>(nameOfEnumCategory: String, enumValues: Array<T>)
        : CustomItemDefinedUuidMgr<T>(nameOfEnumCategory, enumValues) {

    open fun makeNewItemStack(value: T): CraftItemStack {
        value.run {
            var item = this.basicInfo.makeNewItemStack()
            item = parseToRealItemStack(item)
            if (value is DurabilityItem) {
                DurabilityItemWrapper(item).initMaxDurability((value as DurabilityItem).maxDurability)
            }
            if (value is HasUniqueID) {
                UniqueIdAPI.carveUniqueID(item)
            }
            carveEnumUuid(item, this)
            return item
        }
    }

    private fun parseToRealItemStack(item: ItemStack): CraftItemStack {
        return CraftItemStack.asCraftMirror(CraftItemStack.asNMSCopy(item))
    }
}