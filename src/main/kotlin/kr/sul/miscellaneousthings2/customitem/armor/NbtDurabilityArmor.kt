package kr.sul.miscellaneousthings2.customitem.armor

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

// TODO 이거 DurabilityItemWrapper로 이식
class NbtDurabilityArmor private constructor(val item: ItemStack) {
    val nmsItem = (item as CraftItemStack).handle
    val nmsItemTag = nmsItem.tagOrDefault
    fun decreaseCurrentDurability(i: Int) {
        val curDur = nmsItemTag.getInt(CURRENT_DURABILITY_KEY)
        // 파괴
        if (curDur-i <= 0) {
            item.amount -= 1
        }
        nmsItemTag.setInt(CURRENT_DURABILITY_KEY, curDur-i)
        updateLore()
    }
    private fun updateLore() {
        val whatLine = nmsItemTag.getInt(LINE_OF_DURABILITY_LORE_KEY)
        val originLineStr = nmsItemTag.getString(ORIGIN_DURABILITY_LINE_KEY)
        val currentDurability = nmsItemTag.getInt(CURRENT_DURABILITY_KEY)
        val maxDurability = nmsItemTag.getInt(MAX_DURABILITY_KEY)

        val replacedLine = originLineStr.run {
            return@run replace(CURRENT_DURABILITY_LORE, currentDurability.toString())
                .replace(MAX_DURABILITY_LORE, maxDurability.toString())
        }

        val meta = item.itemMeta
        val lore = meta.lore    // TODO 여기서 가끔 NullPointerException 뜨는데
        lore[whatLine] = replacedLine
        meta.lore = lore
        item.itemMeta = meta
    }



    companion object {
        private const val CURRENT_DURABILITY_KEY = "MiscellaneousThings.ArmorCurrentDurability"
        private const val MAX_DURABILITY_KEY = "MiscellaneousThings.ArmorMaxDurability"
        private const val LINE_OF_DURABILITY_LORE_KEY = "MiscellaneousThings.ArmorLineOfDurabilityLore"
        private const val ORIGIN_DURABILITY_LINE_KEY = "MiscellaneousThings.ArmorArmorOriginDurabilityLine" // 내구도 lore 원본 한 줄 ({CURRENT_DURABILITY}, {MAX_DURABILITY} 를 포함하는 원본)

        const val CURRENT_DURABILITY_LORE = "{CUR_DUR}"
        const val MAX_DURABILITY_LORE = "{MAX_DUR}"

        fun initSetting(item: ItemStack, currentDurability: Int, maxDurability: Int): Boolean {
            val nmsItem = (item as CraftItemStack).handle
            val nmsItemTag = nmsItem.tagOrDefault
            var currentDurability = currentDurability
            if (maxDurability < currentDurability) {
                currentDurability = maxDurability
            }
            if (item.lore == null || item.lore!!.isEmpty()) {
                return false
            }
            for ((index, line) in item.lore!!.withIndex()) {
                if (line.contains(CURRENT_DURABILITY_LORE) && line.contains(MAX_DURABILITY_LORE)) {
                    nmsItemTag.setInt(LINE_OF_DURABILITY_LORE_KEY, index)
                    nmsItemTag.setString(ORIGIN_DURABILITY_LINE_KEY, line)
                    break
                }
            }
            // 위의 for문에서 CURRENT_DURABILITY_LORE, MAX_DURABILITY_LORE 가 인식되지 않은 경우
            if (!nmsItemTag.hasKey(LINE_OF_DURABILITY_LORE_KEY)) {
                return false
            }
            nmsItemTag.setInt(CURRENT_DURABILITY_KEY, currentDurability)
            nmsItemTag.setInt(MAX_DURABILITY_KEY, maxDurability)
//            nbti.item.flagIB(ItemFlag.HIDE_ATTRIBUTES)
            constructor(item)!!.updateLore()
            return true
        }
        fun constructor(item: ItemStack): NbtDurabilityArmor? {
            if (!(item as CraftItemStack).handle.tagOrDefault.hasKey(CURRENT_DURABILITY_KEY)) {
                return null
            }
            return NbtDurabilityArmor(item)
        }
    }
}