package kr.sul.miscellaneousthings2.knockdown

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.SimpleNPCDataStore
import net.citizensnpcs.api.util.YamlStorage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import java.io.File

object RideTest: Listener {
    private val npcRegistry = run {
        val dataStore = SimpleNPCDataStore.create(YamlStorage(File(plugin.dataFolder, "Temp file for citizen.yml")))
        CitizensAPI.createAnonymousNPCRegistry(dataStore)!!
    }

    @EventHandler
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        if (!e.player.isOp) {
            return
        }
        if (e.message == "/ride") {
            Bukkit.getPluginManager().registerEvents(KnockdownBody(e.player), plugin)
            e.player.sendMessage("get in ride")
            e.isCancelled = true
        }
    }

    private fun ride(p: Player) {
        val npc = npcRegistry.createNPC(EntityType.BOAT, "Ride")
        npc.setUseMinecraftAI(false)
        npc.spawn(p.location)
        npc.entity.addPassenger(p)
        Bukkit.getScheduler().runTaskLater(plugin, {
            npc.navigator.localParameters.useNewPathfinder(true)
            npc.navigator.localParameters.range(200F)  // setTarget 보다 크게 잡아야 함
            npc.navigator.localParameters.modifiedSpeed(10F)
            npc.navigator.setTarget(Location(p.world, 1690.0, 51.0, 567.0))
        }, 20L)
    }
}