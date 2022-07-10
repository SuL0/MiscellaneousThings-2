package kr.sul.miscellaneousthings2.customitem.food

import kr.sul.miscellaneousthings2.customitem.forenum.CustomItemDefinedMgr
import kr.sul.servercore.util.ItemBuilder.loreIB
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack

object FoodDefinedMgr
        : CustomItemDefinedMgr<FoodDefined>("Food", FoodDefined.values()) {
    override fun makeNewItemStack(value: FoodDefined): CraftItemStack {
        val item = super.makeNewItemStack(value)
        item.loreIB("§7섭취시 회복량")
        item.loreIB("")  // 하트 모양 addHealth 만큼 넣기
        item.loreIB("§7섭취시 포만감")
        item.loreIB("")  // 핫도그 모양 addFoodLevel 만큼 넣기
        value.additionalLore?.forEach { lore ->
            item.loreIB(lore)
        }
        return item
    }
}