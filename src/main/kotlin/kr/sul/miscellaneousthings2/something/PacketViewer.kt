package kr.sul.miscellaneousthings2.something

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

object PacketViewer {
    private val pm = ProtocolLibrary.getProtocolManager()

    fun register() {
        return
//        pm.addPacketListener(object : PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.values()) {
//            override fun onPacketReceiving(e: PacketEvent) {
//                Bukkit.broadcastMessage("§7 - §f${e.packetType.name()}")
//            }
//        })
        pm.addPacketListener(object : PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.values()) {
            override fun onPacketSending(e: PacketEvent) {
                if (e.packetType == PacketType.Play.Server.CHAT
                    || e.packetType == PacketType.Play.Server.TRANSACTION
                    || e.packetType == PacketType.Play.Server.PLAYER_INFO
                    || e.packetType == PacketType.Play.Server.UPDATE_TIME
                    || e.packetType == PacketType.Play.Server.MAP_CHUNK
                    || e.packetType == PacketType.Play.Server.UNLOAD_CHUNK
                ) {
                    return
                }
            }

//            override fun onPacketReceiving(e: PacketEvent) {
//                if (e.packetType == PacketType.Play.Client.POSITION) {
//                    val str = StringUtils.join(e.packet.doubles.values.toTypedArray(), ", ")
//                    Bukkit.broadcastMessage("$str")
//                }
//            }
        })
    }
}