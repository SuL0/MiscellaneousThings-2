package kr.sul.miscellaneousthings2.tutorial.messagehelper

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask


// 메시지가 계속 떠있게끔 함.
// 시간 지나서 메시지가 채팅창에 뜨지 않는 걸 방지 (채팅창을 열지 않은 상태 기준)
// 채팅창은 총 20줄
class KeepMessageInChat(private val p: Player) {
    private var task: BukkitTask? = null

    fun keepThisMessage(fullMsg: FullMessage) {
        task?.cancel()
        task = Bukkit.getScheduler().runTaskTimer(plugin, {
            if (!p.isOnline) {
                destroy()
            }
            fullMsg.send(p)
        }, 0L, 8*20L)
    }

    // destroy()랑 현재는 동일하긴 한데, destroy()를 이것 대신에 쓰기에는 이름 때문에 혼란을 일으킬 수 있음
    fun keepNothing() {
        task?.cancel()
    }

    fun clearChat() {
        for (i in 1..21) {
            p.sendMessage("")
        }
    }

    fun destroy() {
        task?.cancel()
    }
}