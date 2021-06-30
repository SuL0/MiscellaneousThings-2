package kr.sul.miscellaneousthings2.knockdown

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import net.minecraft.server.v1_12_R1.PacketPlayOutBed
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent


object LyingBody: Listener {
   @EventHandler
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        if (e.message == "/deadbody") {
            Bukkit.getPluginManager().registerEvents(KnockdownBody(e.player), plugin)

//            KnockdownBody.npcRegistry.saveToStore()
            e.player.sendMessage("dead body is spawned")
            e.isCancelled = true
        }

       if (e.message == "/lie") {

           val bedLoc = e.player.location.run {
               y = 0.0
               this
           }
           e.player.sendBlockChange(bedLoc, Material.BED_BLOCK, 0)
           (e.player as CraftPlayer).handle.playerConnection.sendPacket(getBedPacket(e.player.entityId, bedLoc))
           e.player.sendMessage("now you're in lay down")
           e.isCancelled = true
       }
    }



    fun getBedPacket(entityId: Int, loc: Location): PacketPlayOutBed {
        val packet = PacketPlayOutBed()
        try {
            val a = packet.javaClass.getDeclaredField("a")
            a.isAccessible = true
            a.setInt(packet, entityId)
            val b = packet.javaClass.getDeclaredField("b")
            b.isAccessible = true
            b.set(packet, net.minecraft.server.v1_12_R1.BlockPosition(loc.blockX, loc.blockY, loc.blockZ))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return packet
    }
}