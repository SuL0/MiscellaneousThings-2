package kr.sul.miscellaneousthings2.customitem

import kr.sul.miscellaneousthings2.Main
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.miscellaneousthings2.customitem.enums.CustomItemDefinedMgr
import kr.sul.miscellaneousthings2.customitem.food.FoodDefined
import kr.sul.miscellaneousthings2.customitem.food.FoodDefinedMgr
import kr.sul.miscellaneousthings2.customitem.food.FoodImpl
import kr.sul.miscellaneousthings2.customitem.itemlistgui.CustomItemListCommand
import kr.sul.miscellaneousthings2.customitem.melee.MeleeWeaponDefined
import kr.sul.miscellaneousthings2.customitem.melee.MeleeWeaponDefinedMgr
import kr.sul.miscellaneousthings2.customitem.melee.MeleeWeaponImpl
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

object CustomItemMain {
    val customItemCategories = listOf(
        CustomItemCategory(MeleeWeaponDefined::class.java, MeleeWeaponDefinedMgr, MeleeWeaponImpl),
        CustomItemCategory(FoodDefined::class.java, FoodDefinedMgr, FoodImpl)
    )

    init {
        for (customItemCategory in customItemCategories) {
            Bukkit.getPluginManager().registerEvents(customItemCategory.customItemImpl, plugin)
        }
//        val map = customItemTypeMap.entries
//            .associate { it.key.enumUuidKey to it.key }
        val list = customItemCategories.map { it.customItemDefineMgr }
        Bukkit.getPluginManager().registerEvents(CheckItemIsUpToDate(list), plugin)
        Main.instance.getCommand("customitem").executor = CustomItemListCommand
    }


    data class CustomItemCategory(
        val customItemDefineClass: Class<*>,
        val customItemDefineMgr: CustomItemDefinedMgr<*>,
        val customItemImpl: Listener
    )
}