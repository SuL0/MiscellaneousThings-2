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
        ItemStackBasicInfo(Material.COOKIE, "§6초코칩 쿠키", null),
        2
    ),
    CAKE("4cc3fe66-2e37-43e5-ac4a-cbc10af6183a",
        ItemStackBasicInfo(Material.CAKE, "§6케이크", null),
        2
    ),
    WATER_MELON("d515334b-2a64-4335-9205-75fe02e5a133",
        ItemStackBasicInfo(Material.MELON, "§6수박", null),
        2
    ),
    CHOCOLATE_PIE("b5b18aae-125f-48d8-9607-dd1bd02f658a",
        ItemStackBasicInfo(Material.CARROT, "§6초코파이", null),
        3
    ),
    RAMYEON("9a35fc31-ff3d-4333-96fc-2d4b09df8b6e",
        ItemStackBasicInfo(Material.APPLE, "§6생 라면", null),
        4
    ),
    ROTTEN_FLESH("1b35e911-cb66-4ec5-8179-607bf9d80d9e",
        ItemStackBasicInfo(Material.ROTTEN_FLESH, "§6썩은 고기", null),
        4
    ),
    COOKED_FISH("7dba382d-fe01-4332-8b72-bfcc1456fe07",
        ItemStackBasicInfo(Material.COOKED_FISH, "§6익힌 생선", null),
        5
    ),
    COOKED_RABBIT("b26ffc4d-7320-4342-bf35-b2f9f871b996",
        ItemStackBasicInfo(Material.COOKED_RABBIT, "§6토끼 고기", null),
        5
    ),
    POTATO_CHIP("043d3770-31ab-4bfa-bede-5437129acb04",
        ItemStackBasicInfo(Material.BAKED_POTATO, "§6감자 칩", null),
        5
    ),
    CHOCOLATE_BAR("b34f13de-c1c7-4b5f-b45e-11df819628c4",
        ItemStackBasicInfo(Material.BREAD, "§6초콜릿 바", null),
        5
    ),
    COOKED_CHICKEN("fd4b95f5-4d8d-45eb-b30b-cafde0059d5d",
        ItemStackBasicInfo(Material.COOKED_CHICKEN, "§6닭고기", null),
        5
    ),
    COOKED_MUTTON("ce30cdc6-03f8-40de-b497-da4a0d737fec",
        ItemStackBasicInfo(Material.COOKED_MUTTON, "§6양고기", null),
        5
    ),
    COOKED_PORK("5d01d80e-b92c-4fb5-8623-22ae56a1a217",
        ItemStackBasicInfo(Material.GRILLED_PORK, "§6돼지고기", null),
        5
    ),
    BOX_OF_CHOCOLATE_PIE("f2503552-dcf9-402a-a711-e1099d1d8683",
        ItemStackBasicInfo(Material.COOKED_BEEF, "§6초코파이 상자", null),
        5
    );
}