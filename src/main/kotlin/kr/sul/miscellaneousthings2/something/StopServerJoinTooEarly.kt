package kr.sul.miscellaneousthings2.something

import kr.sul.servercore.util.UptimeBasedOnTick
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

// 버그 방지용 (e.g. 서버 켜진지 1틱 후 config의 worldName을 기반으로 World 불러오는 등..)
object StopServerJoinTooEarly: Listener {
    @EventHandler
    fun onLogin(e: PlayerLoginEvent) {
        if (UptimeBasedOnTick.uptimeBasedOnTick < 50) {
            e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "§c§l- ISCRAFT -\n\n\n§f서버가 부팅 중입니다. \n§f잠시 후 다시 접속해주십시오.")
        }
    }
}