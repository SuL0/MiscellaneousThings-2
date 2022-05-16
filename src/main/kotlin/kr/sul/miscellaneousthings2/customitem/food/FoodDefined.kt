package kr.sul.miscellaneousthings2.customitem.food

import kr.sul.miscellaneousthings2.customitem.forenum.NormalItem
import kr.sul.miscellaneousthings2.customitem.util.ItemStackBasicInfo
import org.bukkit.Material

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
}