package kr.sul.miscellaneousthings2.something.block

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.file.simplylog.LogLevel
import kr.sul.servercore.file.simplylog.SimplyLog
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object TakeAwayPermissionIfNotOp : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (!e.player.isOp) {
            // Attachment 붙이고 pPerm으로 뭔가를 하다간 이유는 뭔지 몰라도 안되고, 오류남. (아마 Attachment 붙일 때 Plugin을 LuckPerms로 안 해줘서 그런 것 같긴한데)
            if (e.player.hasPermission("*")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${e.player.name} permission unset *")
            }
            if (e.player.hasPermission("op.op")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${e.player.name} permission unset op.op")
            }

            // 로그
            SimplyLog.log(LogLevel.ERROR_CRITICAL, plugin, "플레이어 ${e.player.name} 이 op가 아님에도, 펄미션 슈퍼노드를 소지하고 있었음.")
        }
    }
}