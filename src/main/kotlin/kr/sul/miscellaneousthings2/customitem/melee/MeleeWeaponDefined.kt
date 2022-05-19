package kr.sul.miscellaneousthings2.customitem.melee

import kr.sul.miscellaneousthings2.customitem.forenum.DurabilityItem
import kr.sul.miscellaneousthings2.customitem.forenum.HasUniqueID
import kr.sul.miscellaneousthings2.customitem.util.ItemStackBasicInfo
import org.bukkit.Material


enum class MeleeWeaponDefined(
    override val enumUuid: String,
    val attackDamage: Double,
    val criticalDamage: Double,
    val attackSpeed: Double,  // 단위) 초
    override val maxDurability: Int,
    override val basicInfo: ItemStackBasicInfo,
    val loreFunc: (Int, Int) -> List<String>
): DurabilityItem, HasUniqueID {

    DURABLE_BRANCH("3ef28fb1-47e9-417a-86f5-492dc70ba3ea",
        4.0, 6.0, 0.5, 30,
        ItemStackBasicInfo(Material.GOLD_SWORD,"§f튼튼한 나뭇가지", null),
        { currentDurability: Int, maxDurability: Int ->
            listOf("§f튼튼한 나뭇가지이다.", "", "§7내구도 : §f$currentDurability/$maxDurability")
        }
    ),
    WOODEN_BAT("76d0ea72-1149-4dad-a766-e1ee4f8572cf",
        4.5, 6.75, 0.5, 50,
        ItemStackBasicInfo(Material.GOLD_SWORD,"§f나무 방망이", null),
        { currentDurability: Int, maxDurability: Int ->
            listOf("§7설명란은 공란입니다.", "", "§7내구도 : §f$currentDurability/$maxDurability")
        }
    ),
    WOODEN_SWORD("5243adef-76e8-41db-ac04-b1bdc0a40c91",
        5.0, 7.5, 0.475, 50,
        ItemStackBasicInfo(Material.GOLD_SWORD,"§f목검", null),
        { currentDurability: Int, maxDurability: Int ->
            listOf("§7설명란은 공란입니다.", "", "§7내구도 : §f$currentDurability/$maxDurability")
        }
    ),
    FRYING_PAN("1c8cec46-f162-4e7d-a0f4-4e0e7aa80ed3",
        5.5, 8.25, 0.45, 80,
        ItemStackBasicInfo(Material.GOLD_SWORD,"§f후라이팬", null),
        { currentDurability: Int, maxDurability: Int ->
            listOf("§7설명란은 공란입니다.", "", "§7내구도 : §f$currentDurability/$maxDurability")
        }
    ),
    IRON_PIPE("e5cb9c8e-7d70-4c1c-b86f-ed17a5766dbd",
        6.0, 9.0, 0.45, 80,
        ItemStackBasicInfo(Material.GOLD_SWORD,"§f쇠파이프", null),
        { currentDurability: Int, maxDurability: Int ->
            listOf("§7설명란은 공란입니다.", "", "§7내구도 : §f$currentDurability/$maxDurability")
        }
    ),
    HUNTING_KNIFE("a465c18b-7b91-4edc-bdd0-e4df7c5f58da",
        6.5, 9.75, 0.5, 200,
        ItemStackBasicInfo(Material.GOLD_SWORD,"§f헌팅나이프", null),
        { currentDurability: Int, maxDurability: Int ->
            listOf("§7설명란은 공란입니다.", "", "§7내구도 : §f$currentDurability/$maxDurability")
        }
    ),
    LONG_SWORD("1376fb00-31a2-4787-9040-05f5a47873cd",
        7.0, 10.5, 0.475, 1000,
        ItemStackBasicInfo(Material.GOLD_SWORD,"§f장검", null),
        { currentDurability: Int, maxDurability: Int ->
            listOf("§7설명란은 공란입니다.", "", "§7내구도 : §f$currentDurability/$maxDurability")
        }
    ),
    BEAM_SABER("dc1f5811-c011-4af9-ac2c-b247ebfb2575",
        7.5, 11.25, 0.45, 1000,
        ItemStackBasicInfo(Material.GOLD_SWORD,"§f빔사벨", null),
        { currentDurability: Int, maxDurability: Int ->
            listOf("§7설명란은 공란입니다.", "", "§7내구도 : §f$currentDurability/$maxDurability")
        }
    ),
    LIGHT_SABER("0c43dd39-29f8-45bd-949a-2396c02344f2",
        8.0, 12.0, 0.425, 1000,
        ItemStackBasicInfo(Material.GOLD_SWORD,"§f라이트세이버", null),
        { currentDurability: Int, maxDurability: Int ->
            listOf("§7설명란은 공란입니다.", "", "§7내구도 : §f$currentDurability/$maxDurability")
        }
    ),
    BEAM_X("7b892a4a-2739-4918-a694-2392b2ae3cad",
        8.5, 12.75, 99.99, 1000,
        ItemStackBasicInfo(Material.GOLD_SWORD,"§f빔엑스", null),
        { currentDurability: Int, maxDurability: Int ->
            listOf("§7설명란은 공란입니다.", "", "§7내구도 : §f$currentDurability/$maxDurability")
        }
    );
}