package kr.sul.miscellaneousthings2.chat

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.file.simplylog.LogLevel
import kr.sul.servercore.file.simplylog.SimplyLog
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

// TODO Listener 등록은?
object AreaChat: Listener {
    private const val CHAT_DISTANCE = 100

    @EventHandler
    fun onChat(e: AsyncPlayerChatEvent) {
        return
        e.isCancelled = true
        val p = e.player
        p.sendMessage("§c§lCHAT: §7현재 채팅 관리 시스템이 없습니다.  좋은 아이디어가 있다면 건의해 주세요.")
        return
        val jsonMsg = arrayListOf<TextComponent>().run {
            add(TextComponent("§6[<${CHAT_DISTANCE}M] §f${p.displayName} §f: ${e.message}").run {
                hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent("§e${p.name} §f에게 귓속말 하기 ")))
                clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg ${p.name} ")
                this
            })
            this
        }

        var recieverCnt = 0
        Bukkit.getOnlinePlayers().filter { it.world == p.world && it.location.distance(p.location) <= CHAT_DISTANCE && it != p}.forEach {
            recieverCnt++
            it.sendMessage(*jsonMsg.toTypedArray())
        }
        jsonMsg.add(TextComponent("   §7§o$recieverCnt").run {
            hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent("§7해당 채팅을 수신한 플레이어 수 ")))
            this
        })
        p.sendMessage(*jsonMsg.toTypedArray())

        // 채팅 로그 쌓기
        SimplyLog.log(LogLevel.CHAT, plugin, "[<100M] ${p.name} : ${e.message}")
    }
}