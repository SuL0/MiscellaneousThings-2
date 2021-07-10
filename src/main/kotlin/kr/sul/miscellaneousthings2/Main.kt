package kr.sul.miscellaneousthings2

import kr.sul.miscellaneousthings2.chat.AreaChat
import kr.sul.miscellaneousthings2.chat.ChatInSpawn
import kr.sul.miscellaneousthings2.command.KillAllCommand
import kr.sul.miscellaneousthings2.command.NbtViewCommand
import kr.sul.miscellaneousthings2.endervaultsaddon.SelectorListener
import kr.sul.miscellaneousthings2.knockdown.RideTest
import kr.sul.miscellaneousthings2.mob.spawner.EditMob
import kr.sul.miscellaneousthings2.mob.spawner.MobSpawner
import kr.sul.miscellaneousthings2.something.*
import kr.sul.miscellaneousthings2.warpgui.WarpGUI
import kr.sul.miscellaneousthings2.warpgui.data.WarpPlayerDataMgr
import kr.sul.servercore.util.ObjectInitializer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

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
        WarpPlayerDataMgr.onPluginDisable()
    }

    private fun registerClasses() {
        Bukkit.getPluginManager().registerEvents(StopServerJoinTooEarly, plugin)
        Bukkit.getPluginManager().registerEvents(StopProjectileFromBreakingHanging, plugin)
        Bukkit.getPluginManager().registerEvents(TakeAwayPermissionIfNotOp, plugin)
        Bukkit.getPluginManager().registerEvents(SendResourcePack, plugin)
        Bukkit.getPluginManager().registerEvents(KillAllCommand, plugin)
        Bukkit.getPluginManager().registerEvents(EditMob, plugin)
        Bukkit.getPluginManager().registerEvents(PreventArmorstandFromBreaking, plugin)
        Bukkit.getPluginManager().registerEvents(InvalidateSomeHealAndDamage, plugin)
        Bukkit.getPluginManager().registerEvents(ChatInSpawn, plugin)
        Bukkit.getPluginManager().registerEvents(RideTest, plugin)
        Bukkit.getPluginManager().registerEvents(DamageScouter, plugin)
        Bukkit.getPluginManager().registerEvents(AreaChat, plugin)
        Bukkit.getPluginManager().registerEvents(WarpGUI, plugin)
        Bukkit.getPluginManager().registerEvents(WarpPlayerDataMgr, plugin)
        Bukkit.getPluginManager().registerEvents(CombineArmor, plugin)
        ObjectInitializer.forceInit(MobSpawner::class.java)
        ObjectInitializer.forceInit(SelectorListener::class.java)
        ObjectInitializer.forceInit(FixToDayInSomeWorlds::class.java)

        getCommand("nbtview").executor = NbtViewCommand

        // TODO FOr TEST!
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    // TODO TEST
    @EventHandler
    fun testCommand(e: PlayerCommandPreprocessEvent) {
        if (e.message.startsWith("/dur") && e.message.contains(" ") && e.player.isOp) {
            e.isCancelled = true
            val arg1 = e.message.split(" ")[1].toShort()
            val item = e.player.inventory.itemInMainHand
            item.durability = (item.durability - arg1).toShort()
        }

        if (e.message == "/아이템추출" && e.player.isOp) {
            e.isCancelled = true
            val item = e.player.inventory.itemInMainHand
            Bukkit.getLogger().log(Level.INFO, "-----------------------")
            Bukkit.getLogger().log(Level.INFO, item.itemMeta.displayName.replace("§", "&"))
            item.itemMeta.lore.forEach {
                Bukkit.getLogger().log(Level.INFO, it.replace("§", "&"))
            }
        }
    }
}