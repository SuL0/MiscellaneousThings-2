package kr.sul.miscellaneousthings2.something

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent

object BgmPlayer: Listener {
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

    private fun stopBgm(p: Player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rsp ps ${p.name} master normal")
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rsp ps ${p.name} master test01")
    }
    private fun playBgm(p: Player, name: String, time: Int) {
        stopBgm(p)
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rsp p ${p.name} master $name 1 $time true")
    }
}