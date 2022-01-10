package kr.sul.miscellaneousthings2.knockdown

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.npc.SimpleNPCDataStore
import net.citizensnpcs.api.util.YamlStorage
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.vehicle.VehicleExitEvent
import java.io.File


// npc.navigator.localParameters.useNewPathfinder(true) : 새로운 Pathfinder 알고리즘은 쓰레기
class KnockdownBody(private val p: Player): Listener {
    private lateinit var controllerNPC: NPC
    private lateinit var bodyModeling: Entity
    init {

        makeRideOnController()
    }
    private fun makeRideOnController() {
        controllerNPC = npcRegistry.createNPC(EntityType.ENDERMITE, "Controller")
        controllerNPC.setUseMinecraftAI(false)
        controllerNPC.spawn(p.location)
        controllerNPC.entity.addPassenger(p)
        controllerNPC.entity.isSilent = true
        controllerNPC.navigator.localParameters.range(20F)  // setTarget 보다 크게 잡아야 함
    }



    private fun destroy() {
        HandlerList.unregisterAll(this)
        controllerNPC.destroy()
        bodyModeling.remove()
    }





    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        if (e.entity == p || e.entity == controllerNPC.entity) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onTryToGetOutController(e: VehicleExitEvent) {
        Bukkit.broadcastMessage("VehicleExitEvent")
    }

    companion object {
        private val npcRegistry = run {
            val dataStore = SimpleNPCDataStore.create(YamlStorage(File(plugin.dataFolder, "Temp file for citizen.yml")))
            CitizensAPI.createAnonymousNPCRegistry(dataStore)!!
        }
    }
}