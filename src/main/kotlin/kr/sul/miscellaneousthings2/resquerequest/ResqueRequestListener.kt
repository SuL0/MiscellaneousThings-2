package kr.sul.miscellaneousthings2.resquerequest

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.MsgPrefix
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.DefaultFor
import revxrsal.commands.bukkit.BukkitCommandActor

object ResqueRequestListener {
    @Command("구조요청", "spawn", "넴주")
    fun execute(actor: BukkitCommandActor) {
        val p = actor.asPlayer!!
        val region = ResqueRegion.findRegionGivenLocIsWithin(p.location)
        if (region == null) {
            p.sendMessage("${MsgPrefix.get("RESQUE")}§f구조요청을 보낼 수 있는 장소가 아닙니다.")
            return
        } else {
            p.sendMessage("${MsgPrefix.get("RESQUE")}§f구조요청이 보내진 상태입니다.")   // 구조요청 지역에 들어오면 자동으로 처리되기 때문
            return
        }
    }

    init {
        Bukkit.getScheduler().runTaskTimer(plugin, {
            Bukkit.getOnlinePlayers().forEach { p ->
                val region = ResqueRegion.findRegionGivenLocIsWithin(p.location)
                if (region != null) {
                    if (!region.isInRegistered(p)) {
                        sendResqueRequest(p, region)
                    }
                }
            }
        }, 20L, 20L)
    }

    private fun sendResqueRequest(p: Player, region: ResqueRegion) {
        val request = ResqueRequest(p, region)
        Bukkit.getPluginManager().registerEvents(request, plugin)
    }
}