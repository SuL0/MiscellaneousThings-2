package kr.sul.miscellaneousthings2.something

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent

object SendResourcePack: Listener {
    private const val URL = "https://download.mc-packs.net/pack/d9502a32efd774a102280d3ac9ecfe2d84559f1e.zip"
    private const val HASH = "d9502a32efd774a102280d3ac9ecfe2d84559f1e"


    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val p = e.player
        Bukkit.getScheduler().runTaskLater(plugin, {
            p.sendMessage("")
            p.sendMessage("")
            p.sendMessage("§6§lRP: §f서버 리소스팩을 다운로드 받는 중입니다.")
            p.sendMessage(" §7└ 렉이 발생할 수 있습니다.")
            p.setResourcePack(URL, HASH)  // hash(sha1) 사이트: http://onlinemd5.com/
            // TODO: 일단 스폰에 이동시키기 or 투명 무적 블라인드 걸기. 필요하다면 패킷으로 눈 앞이나 플레이어 머리가 끼이게끔 블럭을 두는 것도 괜찮을 듯?
        }, 5L)
    }

    @EventHandler
    fun onResourcePackReceived(e: PlayerResourcePackStatusEvent) {
        // TODO: 다시 이전 위치로 돌려보내기 or 투명 무적 블라인드 풀기
    }
}