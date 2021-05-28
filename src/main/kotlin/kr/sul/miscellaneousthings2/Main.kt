package kr.sul.miscellaneousthings2

import kr.sul.miscellaneousthings2.command.KillAllCommand
import kr.sul.miscellaneousthings2.command.NbtViewCommand
import kr.sul.miscellaneousthings2.endervaultsaddon.SelectorListener
import kr.sul.miscellaneousthings2.something.SendResourcePack
import kr.sul.miscellaneousthings2.something.StopProjectileBreakingHanging
import kr.sul.miscellaneousthings2.something.StopServerJoinTooEarly
import kr.sul.miscellaneousthings2.something.TakeAwayPermissionIfNotOp
import kr.sul.miscellaneousthings2.zombie.spawner.ZombieSpawner
import kr.sul.servercore.util.ObjectInitializer
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), Listener {
    companion object {
        internal lateinit var plugin: Plugin private set
        internal lateinit var instance: JavaPlugin private set
    }

    override fun onEnable() {
        plugin = this as Plugin
        instance = this
        registerClasses()
    }

    override fun onDisable() {
    }

    private fun registerClasses() {
        Bukkit.getPluginManager().registerEvents(StopServerJoinTooEarly, plugin)
        Bukkit.getPluginManager().registerEvents(StopProjectileBreakingHanging, plugin)
        Bukkit.getPluginManager().registerEvents(TakeAwayPermissionIfNotOp, plugin)
        Bukkit.getPluginManager().registerEvents(SendResourcePack, plugin)
        Bukkit.getPluginManager().registerEvents(KillAllCommand, plugin)
        ObjectInitializer.forceInit(ZombieSpawner::class.java)
        ObjectInitializer.forceInit(SelectorListener::class.java)

        getCommand("nbtview").executor = NbtViewCommand
    }
}