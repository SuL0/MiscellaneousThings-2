package kr.sul.miscellaneousthings2.knockdown

import com.comphenix.protocol.ProtocolLibrary
import com.mojang.authlib.GameProfile
import kr.sul.miscellaneousthings2.Main
import kr.sul.servercore.extensionfunction.PlayerDirection.getFacingDirection
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.SimpleNPCDataStore
import net.citizensnpcs.api.util.YamlStorage
import net.minecraft.server.v1_12_R1.*
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.io.File
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random

// 참조
// https://www.spigotmc.org/threads/spigot-1-8-8-dead-bodies.153870/
// https://github.com/Warren1001/spiritcraft/blob/master/src/main/java/com/kabryxis/spiritcraft/game/DeadBody.java
// CorpseReborn - NMSCorpses_v1_12_2_R1.java *
class KnockdownBody(private val originalP: Player): Listener {
    // default informations
    private val entityId = getNextEntityId()
    private val bodyLoc = originalP.location.clone().add(0.0, 4.0, 0.0)  // 공중에 약간 떴다 떨어지는 모션 위해서 y +4
    private val bedLoc = bodyLoc.clone().run {
        y = random.nextInt(0, 5+1).toDouble()  // 난수를 주는 이유는 똑같은 곳에서 A와 B가 죽게 되면, 같은 좌표(y=0) 에 BED BLOCK을 설치하게 되므로, B의 죽음이 먼저 생성된 A의 시체가 누운 방향에도 변화를 줌
        this
    }

    // some informations related to NMS
    private val dataWatcher = clonePlayerDatawatcher(originalP, entityId).run {
        val obj = DataWatcherObject(10, DataWatcherRegistry.b)
        this.set(obj, 0)
        val obj2 = DataWatcherObject(13, DataWatcherRegistry.a)
        this.set(obj2, 0x7F.toByte())
        this
    }

    private val profile = cloneProfileWithRandomUUID((originalP as CraftPlayer).profile, getModifiedName(originalP.name)) // 16글자 제한

    // packets
    private val infoPacket = PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER).run {
        try {
            val b = this.javaClass.getDeclaredField("b")
            b.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val data = b.get(this) as MutableList<PacketPlayOutPlayerInfo.PlayerInfoData>
            data.add(this.PlayerInfoData(profile, 0, EnumGamemode.SURVIVAL, ChatMessage("")))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        this
    }

    private val spawnPacket = PacketPlayOutNamedEntitySpawn().run {
        try {
            val a = this.javaClass.getDeclaredField("a"); a.isAccessible = true; a.set(this, entityId)
            val b = this.javaClass.getDeclaredField("b"); b.isAccessible = true; b.set(this, profile.id)
            val c = this.javaClass.getDeclaredField("c"); c.isAccessible = true; c.setDouble(this, bodyLoc.x)
            val d = this.javaClass.getDeclaredField("d"); d.isAccessible = true; d.setDouble(this, bodyLoc.y + 1.0f / 16.0f)
            val e = this.javaClass.getDeclaredField("e"); e.isAccessible = true; e.setDouble(this, bodyLoc.z)
            val f = this.javaClass.getDeclaredField("f"); f.isAccessible = true; f.setByte(this, ((bodyLoc.yaw * 256.0f / 360.0f).toInt()).toByte())
            val g = this.javaClass.getDeclaredField("g"); g.isAccessible = true; g.setByte(this, ((bodyLoc.pitch * 256.0f / 360.0f).toInt()).toByte())
            val i = this.javaClass.getDeclaredField("h"); i.isAccessible = true; i.set(this, dataWatcher)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        this
    }
    private val removeInfoPacket = PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER).run {
        try {
            val b = this.javaClass.getDeclaredField("b")
            b.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val data = b.get(this) as MutableList<PacketPlayOutPlayerInfo.PlayerInfoData>
            data.add(this.PlayerInfoData(profile, 0, EnumGamemode.SURVIVAL, ChatMessage("")))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        this
    }
    private val bodyDirection = getByteDirectionOfPlayer(originalP)
    private val sleepInBedPacket = getSleepInBedPacket(entityId, bedLoc)  // 위 패킷들처럼 run { ... } 으로 ?



//    private val npc: NPC = npcRegistry.createNPC(EntityType.PLAYER, originalP.name).run {
//        this.spawn(bodyLoc)  // y=0에 먼저 소환? (스킨 로딩 위함, BED랑은 딱히 상관없음)
//        makeNpcAppearLayDown(bodyLoc.world.players)
//        this
//    }

    // TODO: HitBox

    init {
        spawnBody(originalP.world.players)
    }




    // send packet that NPC lay down
    private fun spawnBody(packetOpponents: List<Player>) {
        packetOpponents.forEach {
            (it as CraftPlayer).handle.playerConnection.sendPacket(infoPacket)
            it.handle.playerConnection.sendPacket(spawnPacket)

            it.sendBlockChange(bedLoc, Material.BED_BLOCK, bodyDirection)
            it.handle.playerConnection.sendPacket(sleepInBedPacket)
        }


/*
        Bukkit.getScheduler().runTask(plugin) {
            packetOpponents.forEach {
                // Faked BED in y = 0
                it.sendBlockChange(bedLoc, Material.BED_BLOCK, bodyDirection)

                // SLEEP on Bed Packet
                (it as CraftPlayer).handle.playerConnection.sendPacket(bedPacket)
            }
        }*/
//        Bukkit.getScheduler().runTaskLater(plugin, {
//            packetOpponents.forEach {
//                (it as CraftPlayer).handle.playerConnection.sendPacket(removeInfoPacket)
//            }
//        }, 20L)  // TODO What does it do?
    }



    fun remove() {
        Bukkit.getOnlinePlayers().forEach {  // TODO 월드 플레이어만 ?
            it.sendBlockChange(bedLoc, bedLoc.block.type, bedLoc.block.data)
        }
    }








    

    companion object {
        private val pm = ProtocolLibrary.getProtocolManager()
        private val random = Random
        val npcRegistry = run {
            val dataStore = SimpleNPCDataStore.create(YamlStorage(File(Main.plugin.dataFolder, "Temp file for citizen.yml")))
            CitizensAPI.createAnonymousNPCRegistry(dataStore)!!
        }
        private val nextFaceOrder: MutableMap<BlockFace, Array<BlockFace>> = EnumMap(BlockFace::class.java)
        // EnumMap : 차이는 key 값으로 Enum을 사용한다는 것 - 이로 인한 성능적 이점
        init {
            nextFaceOrder[BlockFace.NORTH] = arrayOf(BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH)
            nextFaceOrder[BlockFace.EAST] = arrayOf(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST)
            nextFaceOrder[BlockFace.SOUTH] = arrayOf(BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH)
            nextFaceOrder[BlockFace.WEST] = arrayOf(BlockFace.SOUTH, BlockFace.NORTH, BlockFace.EAST)
        }

        private fun clonePlayerDatawatcher(player: Player, currentEntId: Int): DataWatcher {
            val h: EntityHuman = object : EntityHuman(
                    (player.world as CraftWorld).handle,
                    (player as CraftPlayer).profile) {
                override fun sendMessage(arg0: IChatBaseComponent) { return }

                override fun a(arg0: Int, arg1: String): Boolean { return false }

                override fun getChunkCoordinates(): BlockPosition? { return null }

                override fun isSpectator(): Boolean { return false }

                override fun z(): Boolean { return false }
            }
            h.f(currentEntId.toFloat())
            return h.dataWatcher
        }
        fun cloneProfileWithRandomUUID(oldProf: GameProfile, name: String): GameProfile {
            val newProf = GameProfile(UUID.randomUUID(), name)
            newProf.properties.putAll(oldProf.properties)
            return newProf
        }

        private fun getSleepInBedPacket(entityId: Int, loc: Location): PacketPlayOutBed {
            val packet = PacketPlayOutBed()
            try {
                val a = packet.javaClass.getDeclaredField("a")
                a.isAccessible = true
                a.setInt(packet, entityId)
                val b = packet.javaClass.getDeclaredField("b")
                b.isAccessible = true
                b.set(packet, BlockPosition(loc.blockX, loc.blockY, loc.blockZ))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return packet
        }

        private fun getByteDirectionOfPlayer(p: Player): Byte {
            // 주변 블럭 스캔:
            // 만약 플레이어가 마지막으로 본 방향이 블럭으로 막혀있다면, 다른 방향 스캔 해 봄 -> 시체가 온전히 보여지게끔 하기 위함
            val block = p.location.block.getRelative(BlockFace.UP)
            val facing: BlockFace = p.getFacingDirection()
            if (block.getRelative(facing).type.isSolid) {
                val order: Array<BlockFace> = nextFaceOrder[facing]!!
                for (face in order) {
                    if (block.getRelative(face).type.isSolid) continue
                    return getByteDirection(face)
                }
            }
            return getByteDirection(facing)
        }

        private fun getByteDirection(blockFace: BlockFace): Byte {
            return when (blockFace) {
                BlockFace.EAST -> 1
                BlockFace.SOUTH -> 2
                BlockFace.WEST -> 3
                else -> 0
            }
        }

        // 안 쓰는 entity id 반환
        private fun getNextEntityId(): Int {
            return try {
                val entityCount = Entity::class.java.getDeclaredField("entityCount")
                entityCount.isAccessible = true
                val id = entityCount.getInt(null)  // static
                entityCount.setInt(null, id + 1)
                id
            } catch (e: Exception) {
                e.printStackTrace()
                (Math.random() * Int.MAX_VALUE * 0.25).roundToInt()
            }
        }
        private fun getModifiedName(pUsername: String): String {
            var overrideUsername = "$pUsername [기절]"
            if (overrideUsername.length > 16) {  // 16글자 제한
                overrideUsername = overrideUsername.substring(0, 16)
            }
            return overrideUsername
        }
    }
}