package kr.sul.miscellaneousthings2.warptobeachtown

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.extensionfunction.WorldPlayers.getNearbyRealPlayers
import kr.sul.servercore.util.ClassifyWorlds
import me.filoghost.holographicdisplays.api.beta.HolographicDisplaysAPI
import me.filoghost.holographicdisplays.api.beta.hologram.Hologram
import me.filoghost.holographicdisplays.api.beta.hologram.VisibilitySettings
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object WarpToBeachtownWorld: CommandExecutor {
    private const val PREFIX = "§8[ §6이동지점 §8] "
    val warpPointList = arrayListOf<WarpPoint>()

    private val denyNoticePlayerList = arrayListOf<UUID>()
    private val noticeMsg = arrayListOf<TextComponent>().run {
        add(TextComponent("${PREFIX}§7다른 플레이어를 위해 이동해 주시길 부탁드립니다."))
        add(TextComponent(" §8[§6알림 끄기 Click§8]").run {
            hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent("§6알림 끄기")))
            clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/이동지점알림끄기")
            this
        })
        this
    }

    init {
        init()
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (warpPoint in warpPointList) {
                warpPoint.update()
            }
        }, 1L, 3*20L)
    }

    fun warp(p: Player, world: World) {

    }


    class WarpPoint(val num: Int, val warpLoc: Location, val hologramLoc: Location) {
        val hologram: Hologram = HolographicDisplaysAPI.get(plugin).createHologram(hologramLoc)
        var isActived = true
        var cntForMsg = 0
        init {
            hologram.visibilitySettings.globalVisibility = VisibilitySettings.Visibility.VISIBLE
            update()
        }

        fun warp(p: Player) {
            p.teleport(warpLoc)
            update()
        }
        fun update() {
            val nearbyRealPlayers = warpLoc.getNearbyRealPlayers(100.0)
                .sortedBy { hologramLoc.distance(it.location) }
            isActived = nearbyRealPlayers.isEmpty()

            // 3초*5=15초
            if (++cntForMsg % 5 == 0) {
                for (nearbyPlayer in nearbyRealPlayers) {
                    if (denyNoticePlayerList.contains(nearbyPlayer.uniqueId)) {
                        continue
                    }
                    if (warpLoc.distance(nearbyPlayer.location) > 20) {
                        break
                    }
                    nearbyPlayer.sendMessage(*noticeMsg.toTypedArray())
                }
            }

            hologram.lines.clear()
            hologram.lines.run {
                appendText("§6§l${num}번 이동지점")
                appendText("§f§l상태: ${if (isActived) "§a§n활성화" else "§c§n비활성화" }")
                if (!isActived) {
                    appendText("§7(caused by ${nearbyRealPlayers.first().name}")
                }
                appendText("")
                appendText("§f§l[?] §7§o근처에 누군가 있다면")
                appendText("§7§o이 지점은 §c비활성화 §7§o됩니다.")
            }
        }
    }



    private fun init() {
        Bukkit.getScheduler().runTask(plugin) {
            warpPointList.clear()

            val beachTown1 = ClassifyWorlds.beachTownWorlds.first()
            warpPointList.run {
                add(
                    WarpPoint(1,
                    Location(beachTown1, 699.0, 57.0, -70.0, 50.toFloat(), 0.toFloat()),
                    Location(beachTown1, 695.9, 59.5, -66.8))
                )
                add(
                    WarpPoint(2,
                    Location(beachTown1, 451.0, 123.0, 283.0, 0.toFloat(), 0.toFloat()),
                    Location(beachTown1, 451.9, 125.5, 288.0))
                )
                add(
                    WarpPoint(3,
                    Location(beachTown1, 154.0, 66.0, 332.0, (-135).toFloat(), 0.toFloat()),
                    Location(beachTown1, 157.0, 68.5, 329.0))
                )
                add(
                    WarpPoint(4,
                        Location(beachTown1, 280.0, 53.0, -25.0, (-40).toFloat(), 0.toFloat()),
                        Location(beachTown1, 282.0, 55.5, -23.0))
                )
                add(
                    WarpPoint(5,
                        Location(beachTown1, 275.5, 57.0, -124.0, 0.toFloat(), 0.toFloat()),
                        Location(beachTown1, 275.5, 59.5, -131.0))
                )
                add(
                    WarpPoint(6,
                        Location(beachTown1, 303.5, 53.0, -232.5, (-90).toFloat(), 0.toFloat()),
                        Location(beachTown1, 307.5, 55.5, -232.5))
                )
                add(
                    WarpPoint(7,
                        Location(beachTown1, 746.0, 78.0, 199.0, 90.toFloat(), 0.toFloat()),
                        Location(beachTown1, 742.0, 80.5, 199.0))
                )
            }
        }
    }


    // Command 이동지점알림끄기
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return true
        if (denyNoticePlayerList.contains(sender.uniqueId)) {
            sender.sendMessage("${PREFIX}§c이미 알림이 해제 된 상태입니다.")
            return true
        }
        denyNoticePlayerList.add(sender.uniqueId)
        sender.sendMessage("${PREFIX}§f이동지점 알림이 해제되었습니다.")
        return true
    }

    fun onPluginDisable() {
        warpPointList.forEach {
            it.hologram.delete()
        }
    }
}