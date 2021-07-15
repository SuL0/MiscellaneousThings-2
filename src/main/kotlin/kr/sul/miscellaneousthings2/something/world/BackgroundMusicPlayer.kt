package kr.sul.miscellaneousthings2.something.world

import goldbigdragon.github.io.util.PlaySound
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent

object BackgroundMusicPlayer: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (e.player.world.name.startsWith("Normal")) {
            playBgm(e.player, "normal", 16)
        }
        else if (e.player.world.name == "TestWorld") {
            playBgm(e.player, "test01", 24)
        }
        else {
            stopBgm(e.player)        // TODO 개선
        }
    }
    @EventHandler
    fun onChangeWorld(e: PlayerChangedWorldEvent) {
        if (e.player.world.name.startsWith("Normal")) {
            playBgm(e.player, "normal", 16)
        }
        else if (e.player.world.name == "TestWorld") {
            playBgm(e.player, "test01", 24)
        }
        else {
            stopBgm(e.player)        // TODO 개선
        }
    }

    // PlaySound.stop(p.name, "records", "record.13")
    // PlaySound.play(p.uniqueId, "records", "record.far", 1.0F, 15, false)
    private fun stopBgm(p: Player) {
        PlaySound.stop(p.uniqueId, "master", "normal")
        PlaySound.stop(p.uniqueId, "master", "test01")
    }
    private fun playBgm(p: Player, name: String, time: Int) {
        stopBgm(p)
        PlaySound.play(p.uniqueId, "master", name, 1F, time, true)
    }
}