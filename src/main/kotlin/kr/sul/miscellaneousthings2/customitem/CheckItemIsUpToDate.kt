package kr.sul.miscellaneousthings2.customitem

import kr.sul.miscellaneousthings2.customitem.forenum.CustomItemDefinedMgr
import kr.sul.miscellaneousthings2.customitem.forenum.DurabilityItem
import kr.sul.miscellaneousthings2.customitem.forenum.NormalItem
import kr.sul.miscellaneousthings2.customitem.util.DurabilityItemWrapper
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.event.Listener

// Armor같은 건 성능이 아이템에 박혀있어서 아이템 착용할 때마다 확인 해 줘야하고,
// 나머지 아이템들은 lore과 내구도정도라서 비교적 덜 민감함
class CheckItemIsUpToDate(private val enumCustomItemCategorySet: List<CustomItemDefinedMgr<*>>)
        : Listener {
    private fun check(item: CraftItemStack) {
        val enumFound = findMatchingIteratingAllCategory(item) ?: return
        if (enumFound is DurabilityItem) {
            if (enumFound.maxDurability != DurabilityItemWrapper(item).maxDurability) {
                DurabilityItemWrapper(item).editMaxDurability(enumFound.maxDurability)
            }
        }
        // 방어구 버전 체크
    }
    private fun findMatchingIteratingAllCategory(item: CraftItemStack): NormalItem? {
        for (enumCustomItemCategory in enumCustomItemCategorySet) {
            val find = enumCustomItemCategory.findMatching(item)
            if (find != null) {
                return find
            }
        }
        return null
    }






    init {
        INSTANCE = this
    }
    companion object {
        lateinit var INSTANCE: CheckItemIsUpToDate
    }
}