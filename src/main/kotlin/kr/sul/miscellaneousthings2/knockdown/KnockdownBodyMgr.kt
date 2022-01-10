package kr.sul.miscellaneousthings2.knockdown

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import kr.sul.miscellaneousthings2.Main

object KnockdownBodyMgr {
    private val pm = ProtocolLibrary.getProtocolManager()
    private val knockdownBodies = arrayListOf<KnockdownBody>()


    // 걍 Class 객체마다 addPacketListener 하는 편이 편하지 않나?
//    init {
//        pm.addPacketListener(object: PacketAdapter(Main.plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.STEER_VEHICLE) {
//            override fun onPacketReceiving(e: PacketEvent) {
//
//                // if
//                val sideway = e.packet.float.read(0)  // (+)좌, (-)우
//                val forward = e.packet.float.read(1)  // (+)전, (-)후
//
//                if (sideway != 0.0F || forward != 0.0F) {
//                    val vec = p.location.direction.normalize().multiply(10)
//                    val destination = controllerNPC.entity.location.clone().add(vec)
//                    controllerNPC.navigator.setTarget(destination)
//                }
//            }
//        })
//    }
//
//
//    fun spawnKnockdownBody(p: Player) {
//
//    }
}