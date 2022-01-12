package kr.sul.miscellaneousthings2.something

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.UptimeBasedOnTick
import org.bukkit.Bukkit
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent

/**
 * 몹을 때릴 시 2초 안에 F를 누름으로서 대시할 수 있음
 */
/*
object HitAndDash: Listener {
    private const val TIME = 2*20L  // F키 누르기까지 시간 제한
    private val latestHitMobTime = hashMapOf<Player, Long>()  // (Player, TimeStamp) - 가장 마지막 몹을 때린 시간

    init {
        Bukkit.getScheduler().runTaskTimer(plugin, {
            val toRemove = arrayListOf<Player>()
            latestHitMobTime.forEach {
                val p = it.key
                val leftTimeTick = TIME - (UptimeBasedOnTick.uptimeBasedOnTick - it.value)
                if (leftTimeTick > 0) {
                    val sec = (leftTimeTick*10/20)/10.0  // (*10, /10을 통해 소수점 한자리까지만 땜)
                    p.sendActionBar("§f§l[F] §7${sec}s")
                } else {
                    toRemove.add(it.key)
                }
            }

            // TIME 보다 시간이 더 흐른 Player들은 Map에서 제거
            toRemove.forEach {
                it.sendActionBar("§f")
                latestHitMobTime.remove(it)
            }
        }, 2L, 2L)
    }

    private fun dash(p: Player) {
        p.sendActionBar("§f")
        latestHitMobTime.remove(p)
        p.velocity = p.location.direction.clone().normalize()
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onHit(e: EntityDamageByEntityEvent) {
        if (e.isCancelled) return
        if (e.damager is Player && e.entity is Monster) {
            latestHitMobTime[e.damager as Player] = UptimeBasedOnTick.uptimeBasedOnTick
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)  // cancel이 되든 말든 F키를 누르면 발동
    fun onF(e: PlayerSwapHandItemsEvent) {
        if (latestHitMobTime.contains(e.player)) {
            dash(e.player)
        }
    }



    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        latestHitMobTime.remove(e.player)
    }
}*/
