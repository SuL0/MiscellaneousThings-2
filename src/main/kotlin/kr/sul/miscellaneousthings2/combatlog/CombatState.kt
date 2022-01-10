package kr.sul.miscellaneousthings2.combatlog

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.MsgPrefix
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitTask

class CombatState(val p: Player): Listener {
    val damageTakenReport = DamageTakenReport(p).startToReport()
    private var taskToDestroyThisObj: BukkitTask? = null
    companion object {
        private const val TIME_TO_DESTROY = 30*20L  // 파괴하기 까지 걸릴 시간
    }

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        updateWhenToDestroyThisObj()
        // 마지막 데미지로부터 30초가 지났으면 전투상태 해제시켜줘야 함. runTaskLater을 계속 취소하면 될 듯
        p.sendMessage("")
        p.sendMessage("${MsgPrefix.get("COMBAT")}§c전투상태 §f에 돌입하였습니다.")
        p.sendMessage("${MsgPrefix.get("COMBAT")}§f전투상태는 §6${TIME_TO_DESTROY/20}초§f간 지속되며, 전투상태인 중에 §4로그아웃 시 사망 §f처리됩니다.")
    }



    @EventHandler(priority = EventPriority.MONITOR)
    fun onDeath(e: PlayerDeathEvent) {
        if (e.isCancelled) return
        // killer이 없을 수 있음
        val killer = e.entity.killer  // TODO 총알로 죽였으면, killer은 총을 쏜 사람? or 총알?
        e.isCancelled = true
        destroy()
        DeadState(e.entity, killer, e.drops, damageTakenReport)
    }


    // TODO 서버가 종료되어 로그아웃된건 아닌지 확인할 필요가 있음.
    @EventHandler
    fun onLogOut(e: PlayerQuitEvent) {
        if (e.player == p) {
            destroy()
            p.health = 0.0
            // player이 일방적으로 때리고 아무에게도 맞지 않은 채로 나가면 killer가 없을 수 있음
            if (p.killer != null) {
                p.killer.sendMessage("${MsgPrefix.get("COMBAT")}§e${e.player.name} §f이(가) 당신과 전투 중 §c로그아웃 §f하여 §4사망 §f처리되었습니다.")
            }
            // 나중에 들어왔을 때 사인 알려줘야 하나?
        }
    }


    private fun updateWhenToDestroyThisObj() {
        taskToDestroyThisObj?.cancel()
        taskToDestroyThisObj = Bukkit.getScheduler().runTaskLater(plugin, {
            destroy()
        }, TIME_TO_DESTROY)
//        taskToDestroyThisObj = object : BukkitRunnable() {
//            override fun run() {
//            }
//        }
    }
    private fun destroy() {
        taskToDestroyThisObj?.cancel()
        HandlerList.unregisterAll(this)
        damageTakenReport.stopToReport()
        CombatLog.onCombatStatusDestroyed(this)
        p.sendMessage("")
        p.sendMessage("${MsgPrefix.get("COMBAT")}§c전투상태 §f가 해제되었습니다.")
    }
}