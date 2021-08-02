package kr.sul.miscellaneousthings2.command

import kr.sul.servercore.ServerCore.Companion.economy
import kr.sul.servercore.ServerCore.Companion.isEconomyEnabled
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object EconomyCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return false
        val p = sender as Player
        if (!isEconomyEnabled) {
            p.sendMessage("§c§lECON: §7현재 이코노미가 §c비활성화 §7상태입니다.")
            return true
        }
        p.sendMessage("§a잔고: §f${economy.getBalance(p)}원")
        return true
    }
}