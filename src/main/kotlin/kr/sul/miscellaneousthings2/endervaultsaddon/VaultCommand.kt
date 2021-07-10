package kr.sul.miscellaneousthings2.endervaultsaddon

import kr.sul.servercore.util.ClassifyWorlds
import kr.sul.servercore.util.SimplyLuckPerm
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent

object VaultCommand: Listener {
    private const val USE_ENDERVAULT_PERMISSION = "endervaults.command.use"

    @EventHandler(priority = EventPriority.HIGH)
    fun onJoin(e: PlayerJoinEvent) {
        if (!SimplyLuckPerm.hasPermission(e.player, USE_ENDERVAULT_PERMISSION)) {
            SimplyLuckPerm.addPermission(e.player.uniqueId, USE_ENDERVAULT_PERMISSION)
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        if (e.isCancelled) return
        val p = e.player
        if (e.message.startsWith("vaults")) {
            e.isCancelled = true
        }
        else if (e.message == "/창고") {
            e.isCancelled = true
            p.performCommand("/pv")
        }
        else if (e.message == "/pv") {
            if (!ClassifyWorlds.isSpawnWorld(p.world)) {
                e.isCancelled = true
                p.sendMessage("§c§lVAULT: §7창고는 §f스폰 §7에서만 사용이 가능합니다.")
            }
        }
    }
}