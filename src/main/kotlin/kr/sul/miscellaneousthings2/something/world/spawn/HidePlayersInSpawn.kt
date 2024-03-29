package kr.sul.miscellaneousthings2.something.world.spawn

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.extensionfunction.WorldPlayers.getRealPlayers
import kr.sul.servercore.extensionfunction.WorldPlayers.getRealPlayersExcept
import kr.sul.servercore.util.ClassifyWorlds
import kr.sul.servercore.util.PlayerDistance.distance
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/*
object HidePlayersInSpawn: Listener {
    val RENDER_DISTANCE = run {
        val file = File("${Bukkit.getServer().worldContainer}/spigot.yml")
        val yaml = YamlConfiguration.loadConfiguration(file)
        return@run yaml.getInt("world-settings.default.entity-tracking-range.players")
    }

    private val showingMap = hashMapOf<Player, ArrayList<Player>>()  // 스폰에서 Key가 볼 수 있는 플레이어 목록
    private const val DISTANCE = 10

    // Citizen NPC는 월드가 로딩될 때 약 1초간 서버에 존재하다가, 없는 플레이어 처리됨 (그래서 world.players에서 약 1초간 NPC가 Player로 인식돼서 뜰 수 있음)
    init {
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (spawnWorld in ClassifyWorlds.spawnWorlds) {
                for (p in spawnWorld.getRealPlayers()) {

                    // showed 에 있는 Values(플레이어들) 이 조건을 충족하고 있는지 확인
                    if (showingMap.containsKey(p)) {
                        val toRemove = arrayListOf<Player>()
                        for (showingP in showingMap[p]!!) {
                            if (!(showingP.isOnline && showingP.world == p.world
                                        && showingP.distance(p) <= DISTANCE)) {
                                toRemove.add(showingP)
                                p.hidePlayer(plugin, showingP)
                            }
                        }
                        showingMap[p]!!.removeAll(toRemove)
                    }

                    // 주변 플레이어는 show
                    for (nearbyP in spawnWorld.getRealPlayersExcept(p).filter { it.distance(p) <= DISTANCE }) {
                        if (!showingMap.containsKey(p)) {
                            showingMap[p] = arrayListOf()
                        }
                        if (!showingMap[p]!!.contains(nearbyP)) {
                            p.showPlayer(plugin, nearbyP)
                            showingMap[p]!!.add(nearbyP)
                        }
                    }
                }
            }
        }, 10L, 10L)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        showAllPlayers(e.player)

        if (ClassifyWorlds.isSpawnWorld(e.player.world)) {
            onArrivedAtSpawn(e.player)
        }
    }
    @EventHandler
    fun onChangedWorld(e: PlayerChangedWorldEvent) {
        val p = e.player
        if (ClassifyWorlds.isSpawnWorld(p.world)) {
            onArrivedAtSpawn(p)
        } else if (ClassifyWorlds.isSpawnWorld(e.from)) {
            if (showingMap.containsKey(p)) {
                showingMap.remove(p)
            }
            showAllPlayers(p)
        }
    }

    // 모두를 드러내기, 나도 모두에게 드러내기
    private fun showAllPlayers(p: Player) {
        for (loopP in Bukkit.getOnlinePlayers().filter { it != p }) {
            if (!p.canSee(loopP)) p.showPlayer(plugin, loopP)
            if (!loopP.canSee(p)) loopP.showPlayer(plugin, p)
        }
    }
    private fun onArrivedAtSpawn(p: Player) {
        p.world.getRealPlayersExcept(p).forEach { loopP ->
            p.hidePlayer(plugin, loopP)
            loopP.hidePlayer(plugin, p)
        }
    }


    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        if (showingMap.containsKey(e.player)) {
            showingMap.remove(e.player)
        }
        showAllPlayers(e.player)
    }


    @EventHandler
    fun onCmd(e: PlayerCommandPreprocessEvent) {
        if (e.message == "/showedMap" && e.player.isOp) {
            e.player.sendMessage("${showingMap[e.player]}")
            e.isCancelled = true
        }
    }
}*/