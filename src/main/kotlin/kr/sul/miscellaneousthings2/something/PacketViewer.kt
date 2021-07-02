package kr.sul.miscellaneousthings2.something

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit

object PacketViewer {
    private val pm = ProtocolLibrary.getProtocolManager()

    fun register() {
        pm.addPacketListener(object : PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.values()) {
            override fun onPacketReceiving(e: PacketEvent) {
                if (e.packetType != PacketType.Play.Client.POSITION && e.packetType != PacketType.Play.Client.POSITION_LOOK && e.packetType != PacketType.Play.Client.LOOK) {  // 도배하는 패킷
                    Bukkit.broadcastMessage("§7 - §f${e.packetType.name()}")
                }
            }
        })
    }
}