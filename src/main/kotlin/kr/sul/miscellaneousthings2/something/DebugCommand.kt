package kr.sul.miscellaneousthings2.something

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.MsgPrefix
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object DebugCommand: Listener {
    @EventHandler
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        if (e.message.lowercase() == "/debug" || e.message.lowercase() == "/debug ") {
            if (!e.player.isOp) return
            e.isCancelled = true
            sendHelpMessage(e.player)
        }
        else if (e.message.startsWith("/debug ", true)) {
            if (!e.player.isOp) return
            e.isCancelled = true
            val args = e.message.substring("/debug ".length).split(" ")
            when (args[0].lowercase()) {
                // TODO 1틱만에 나갔다 들어오는거 문제 없으려나? 특히 DB를 Async로 작업하는 플러그인들
                "onJoin".lowercase() -> {
                    val onQuitEvent = PlayerQuitEvent(e.player, "")
                    Bukkit.getPluginManager().callEvent(onQuitEvent)
                    Bukkit.getScheduler().runTask(plugin) {
                        val onJoinEvent = PlayerJoinEvent(e.player, "")
                        Bukkit.getPluginManager().callEvent(onJoinEvent)
                    }
                }
                else -> {
                    sendHelpMessage(e.player)
                }
            }
        }
    }

    private fun sendHelpMessage(p: Player) {
        p.sendMessage("${MsgPrefix.get("DEBUG")}/debug onJoin §7- onQuit -> onJoin(1틱 후) 이벤트를 강제로 호출")
//        p.sendMessage("${MsgPrefix.get("DEBUG")}")
    }
}