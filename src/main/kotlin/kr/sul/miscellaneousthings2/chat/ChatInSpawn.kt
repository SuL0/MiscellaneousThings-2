package kr.sul.miscellaneousthings2.chat

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerQuitEvent

object ChatInSpawn: Listener {
    private val lastestChat = hashMapOf<Player, Long>()
    private const val CHAT_DELAY = 15L // s

    @EventHandler(priority = EventPriority.LOW)
    fun onChat(e: AsyncPlayerChatEvent) {
        val p = e.player
        if (lastestChat.containsKey(p)
                && (System.currentTimeMillis() - lastestChat[p]!!)/1000 == CHAT_DELAY) {
            e.isCancelled = true
        }
        lastestChat[e.player] = System.currentTimeMillis()
    }


    @EventHandler(priority = EventPriority.LOW)
    fun onQuit(e: PlayerQuitEvent) {
        lastestChat.remove(e.player)
    }

    // TODO: 스폰인지 구분
}