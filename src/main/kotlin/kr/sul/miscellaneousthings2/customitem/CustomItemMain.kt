package kr.sul.miscellaneousthings2.customitem

import kr.sul.miscellaneousthings2.Main
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.miscellaneousthings2.customitem.forenum.CustomItemDefinedMgr
import kr.sul.miscellaneousthings2.customitem.forenum.NormalItem
import kr.sul.miscellaneousthings2.customitem.food.FoodDefined
import kr.sul.miscellaneousthings2.customitem.food.FoodDefinedMgr
import kr.sul.miscellaneousthings2.customitem.food.FoodImpl
import kr.sul.miscellaneousthings2.customitem.itemlistgui.CustomItemListCommand
import kr.sul.miscellaneousthings2.customitem.melee.MeleeWeaponDefined
import kr.sul.miscellaneousthings2.customitem.melee.MeleeWeaponDefinedMgr
import kr.sul.miscellaneousthings2.customitem.melee.MeleeWeaponImpl
import org.bukkit.Bukkit
import org.bukkit.event.Listener

object CustomItemMain {
    val customItemCategories = listOf(
        CustomItemCategory(MeleeWeaponDefined::class.java, MeleeWeaponDefinedMgr as CustomItemDefinedMgr<NormalItem>, MeleeWeaponImpl),
        CustomItemCategory(FoodDefined::class.java, FoodDefinedMgr as CustomItemDefinedMgr<NormalItem>, FoodImpl)
    )

    init {
        for (customItemCategory in customItemCategories) {
            Bukkit.getPluginManager().registerEvents(customItemCategory.customItemImpl, plugin)
        }
//        val map = customItemTypeMap.entries
//            .associate { it.key.enumUuidKey to it.key }
        val list = customItemCategories.map { it.customItemDefinedMgr }
        Bukkit.getPluginManager().registerEvents(CheckItemIsUpToDate(list), plugin)
        Main.instance.getCommand("customitems").executor = CustomItemListCommand
    }


    data class CustomItemCategory(
        val customItemDefinedClass: Class<*>,
        val customItemDefinedMgr: CustomItemDefinedMgr<NormalItem>,
        val customItemImpl: Listener
    )
}