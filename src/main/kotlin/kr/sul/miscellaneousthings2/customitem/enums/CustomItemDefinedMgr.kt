package kr.sul.miscellaneousthings2.customitem.enums

import kr.sul.miscellaneousthings2.customitem.util.CustomItemUtil
import kr.sul.miscellaneousthings2.customitem.util.DurabilityItemWrapper
import kr.sul.servercore.util.UniqueIdAPI
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack

open class CustomItemDefinedMgr<T: NormalItem>(nameOfEnumType: String, enumValues: Array<T>)
        : CustomItemDefinedUuidMgr<T>(nameOfEnumType, enumValues) {

    open fun makeNewItemStack(value: T): CraftItemStack {
        value.run {
            var item = this.basicInfo.makeNewItemStack()
            item = CustomItemUtil.parseToRealItemStack(item)
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
}