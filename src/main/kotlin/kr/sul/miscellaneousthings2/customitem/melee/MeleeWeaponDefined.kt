package kr.sul.miscellaneousthings2.customitem.melee

import kr.sul.miscellaneousthings2.customitem.enums.CustomItemDefinedMgr
import kr.sul.miscellaneousthings2.customitem.enums.DurabilityItem
import kr.sul.miscellaneousthings2.customitem.enums.HasUniqueID
import kr.sul.miscellaneousthings2.customitem.util.ItemStackBasicInfo
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack


enum class MeleeWeaponDefined(
    override val enumUuid: String,
    val attackDamage: Double,
    val criticalDamage: Double,
    val attackSpeed: Double,
    override val maxDurability: Int,
    override val basicInfo: ItemStackBasicInfo,
    val loreFunc: (Int, Int) -> List<String>
): DurabilityItem, HasUniqueID {

    DURABLE_BRANCH("3ef28fb1-47e9-417a-86f5-492dc70ba3ea",
        1.0, 1.0, 0.2, 10,
        ItemStackBasicInfo(Material.WOOD_SWORD,"§f튼튼한 나뭇가지", null),
        { currentDurability: Int, maxDurability: Int ->
            listOf("§f튼튼한 나뭇가지이다.", "", "§7내구도 : §f$currentDurability/$maxDurability")
        }
    );

//    DURABLE_BRANCH("3ef28fb1-47e9-417a-86f5-492dc70ba3ea",
//    ItemStackBasicInfo2(Material.WOOD_SWORD, "§f튼튼한 나뭇가지", listOf("§f튼튼한 나뭇가지이다.")),
//    1, 1, 0.2, 10
//    );

    companion object: CustomItemDefinedMgr<MeleeWeaponDefined>("Melee", values()) {
        override fun makeNewItemStack(value: MeleeWeaponDefined): CraftItemStack {
            val item = super.makeNewItemStack(value)
            item.lore = value.loreFunc.invoke(value.maxDurability, value.maxDurability)
            return item
        }
    }
}