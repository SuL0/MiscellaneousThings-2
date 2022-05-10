package kr.sul.miscellaneousthings2.customitem

import kr.sul.miscellaneousthings2.customitem.food.FoodImpl
import kr.sul.miscellaneousthings2.customitem.melee.MeleeWeaponImpl
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

object CustomItemMain {
    private var bInit = false
    fun init(plugin: Plugin) {
        if (bInit) throw Exception("중복되는 initialize")
        bInit = true
        Bukkit.getPluginManager().registerEvents(MeleeWeaponImpl, plugin)
        Bukkit.getPluginManager().registerEvents(FoodImpl, plugin)
    }
}