package kr.sul.miscellaneousthings2

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.npc.SimpleNPCDataStore
import net.citizensnpcs.api.util.YamlStorage
import net.minecraft.server.v1_12_R1.EntityHuman
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftZombie
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.io.File

object NpcTest {
    private val npcRegistry = run {
        val dataStore = SimpleNPCDataStore.create(YamlStorage(File(Main.plugin.dataFolder, "Temp file for citizen.yml")))
        CitizensAPI.createAnonymousNPCRegistry(dataStore)!!
    }
    private val npcList = arrayListOf<NPC>()

    fun run(p: Player) {
//        val zom = p.world.spawnEntity(p.location, EntityType.ZOMBIE) as org.bukkit.entity.Zombie
//        zom.isBaby = true
        npcList.add(Zombie(p, p.location).npc)
//        Bukkit.getScheduler().runTaskLater(Main.plugin, {
//            npc.navigator.setTarget(Location(p.world, 846.0, 51.0, 835.0))
//        }, 20L)
    }

    init {
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (npc in npcList) {
                if (!npc.isSpawned) {
                    npcList.remove(npc)
                }


                // 이 구문은 NPC랑 구분 못 함
                val nearby = npc.entity.location.getNearbyPlayers(50.0, 40.0)
                    .sortedBy { npc.entity.location.distance(it.location) }
                if (nearby.isEmpty()) {
                    npc.destroy()
                } else {
                    npc.navigator.setTarget(nearby.first(), true)
                    // TODO 길찾기 전략도 설정 가능
                }
            }
        }, 0L, 20L)



        // 포격
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (npc in npcList) {
                if (!npc.isSpawned) {
                    npcList.remove(npc)
                }


                // 이 구문은 NPC랑 구분 못 함
                val nearby = npc.entity.location.getNearbyPlayers(50.0, 40.0)
                    .sortedBy { npc.entity.location.distance(it.location) }
                if (nearby.isEmpty()) {
                    npc.destroy()
                } else {
                    npc.navigator.setTarget(nearby.first(), true)
                    // TODO 길찾기 전략도 설정 가능
                }
            }
        }, 0L, 20L)

    }

    class Zombie(p: Player, loc: Location) {
        val npc: NPC = npcRegistry.createNPC(EntityType.ZOMBIE, "Test Mob")
        private val nmsNpc
            get() = (npc.entity as CraftZombie).handle
        init {
            npc.setUseMinecraftAI(false)
//            npc.setUseMinecraftAI(true) // 테스트?
            npc.spawn(loc)
            npc.navigator.localParameters.useNewPathfinder(false)
            npc.navigator.localParameters.range(50F)  // setTarget 보다 크게 잡아야 함
            npc.navigator.localParameters.modifiedSpeed(3F)
            npc.isProtected = false
//            addRandomLookAround()
        }

        // 필요가 있나?
        private fun addRandomLookAround() {
//            EntityZombie
            nmsNpc.run {
                goalSelector.a(8, PathfinderGoalLookAtPlayer(this, EntityHuman::class.java, 8.0f))
                goalSelector.a(8, PathfinderGoalRandomLookaround(this))
            }
//            npc.defaultGoalController.addGoal(Goal)
        }
    }
}