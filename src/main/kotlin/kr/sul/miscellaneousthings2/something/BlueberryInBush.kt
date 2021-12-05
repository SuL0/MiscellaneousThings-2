package kr.sul.miscellaneousthings2.something

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.miscellaneousthings2.something.block.WorldProtect
import kr.sul.servercore.util.ProtocolManager.pm
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import kotlin.random.Random


object BlueberryInBush: Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onBreakBush(e: BlockBreakEvent) {
        if (e.player.isOp && e.player.gameMode == GameMode.CREATIVE) return // 진짜로 풀을 부술 때는 블루베리 뜨면 안됨
        if (e.block.type == Material.LONG_GRASS) {
            if (Random.nextFloat() < 0.05) {
                // TODO 블루베리 드랍
//                e.block.world.dropItem(e.block.location, ItemStack())
            }
//            Bukkit.getScheduler().runTask(plugin) {
//                val destroyPacket = pm.createPacket(PacketType.Play.Server.BLOCK_CHANGE)
//                destroyPacket.run {
//                    blockPositionModifier.write(0, BlockPosition(e.block.x, e.block.y, e.block.z))
//                    blockData.write(0, WrappedBlockData.createData(Material.AIR))
//                }
//                pm.sendServerPacket(e.player, destroyPacket)
//            }
//            Bukkit.getScheduler().runTask(plugin) {
//                e.player.sendBlockChange(e.block.location, Material.AIR, 0)
//            }
        }
    }

    init {
        // 풀을 부쉈을 때 실제로는 블럭보호 코드 중 e.isCancelled로 인해 블럭이 부숴진 것이 롤백되지만,
        // 아래 기능을 통해 블럭이 롤백되는 패킷(풀 블럭 설치 패킷)을 가로챔으로서 실제로 부서진 것 처럼 만듦
        pm.addPacketListener(object : PacketAdapter(plugin, PacketType.Play.Server.BLOCK_CHANGE) {
            override fun onPacketSending(e: PacketEvent) {
                val type = e.packet.blockData.values.first().type
                if (type == Material.LONG_GRASS || type == Material.DOUBLE_PLANT) {
                    if (WorldProtect.isInWorldProtectBypassMode(e.player)) {
                        return // 풀 블럭을 정말로 직접 설치했으면 return
                    }
                    e.isCancelled = true
                }
            }
        })
    }
}