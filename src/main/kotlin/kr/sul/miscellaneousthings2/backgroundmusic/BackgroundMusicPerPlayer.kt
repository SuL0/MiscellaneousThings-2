package kr.sul.miscellaneousthings2.backgroundmusic

import goldbigdragon.github.io.util.PlaySound
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable

object BackgroundMusicPerPlayer : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val p = e.player
        object : BukkitRunnable() {
            override fun run() {
                if (!p.isOnline) {
                    cancel()
                    return
                }
                updateMusic(p)
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 15*20L)
    }

    @EventHandler
    fun onChangedWorld(e: PlayerChangedWorldEvent) {
        updateMusic(e.player)
    }



    private fun updateMusic(p: Player) {
        if (isDay(p.world)) {
            PlaySound.stop(p.name, "records", "record.13")
            PlaySound.play(p.uniqueId, "records", "record.far", 1.0F, 15, false)
        } else {
            PlaySound.stop(p.name, "records", "record.far")
            PlaySound.play(p.uniqueId, "records", "record.13", 1.0F, 15, false)
        }
    }

    private fun isDay(world: World): Boolean {
        val time = world.time
        return (time < 12300 || time > 23850)
    }
}