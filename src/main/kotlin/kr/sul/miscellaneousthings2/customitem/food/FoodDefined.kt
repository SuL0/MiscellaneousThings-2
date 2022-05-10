package kr.sul.miscellaneousthings2.customitem.food

import kr.sul.miscellaneousthings2.customitem.enums.CustomItemDefinedMgr
import kr.sul.miscellaneousthings2.customitem.enums.NormalItem
import kr.sul.miscellaneousthings2.customitem.util.ItemStackBasicInfo
import kr.sul.servercore.util.ItemBuilder.loreIB
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack

enum class FoodDefined(
    override val enumUuid: String,
    override val basicInfo: ItemStackBasicInfo,
    val addFoodLevel: Int,  // addFoodLevel : addHealth = 4 : 1   1에 반칸
    val additionalLore: List<String>? = null
): NormalItem {

    COOKIE("bea9fb28-e181-4150-b66e-dbbfcad3bf3a",
        ItemStackBasicInfo(Material.COOKIE, "§6쿠키", null),
        2
    ),
    CAKE("4cc3fe66-2e37-43e5-ac4a-cbc10af6183a",
        ItemStackBasicInfo(Material.CAKE, "§6케이크", null),
        2
    ),
    WATER_MELON("d515334b-2a64-4335-9205-75fe02e5a133",
        ItemStackBasicInfo(Material.MELON, "§6수박", null),
        2
    );




    // https://pjh3749.tistory.com/279 로 개선 여지 있음
    companion object: CustomItemDefinedMgr<FoodDefined>("Food", values()) {
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

//
//        fun find(material: Material, itemName: String): FoodDefined? {
//            return values().find { it.material == material && it.itemName == itemName }
//        }
    }
}