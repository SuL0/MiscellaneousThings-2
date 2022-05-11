package kr.sul.miscellaneousthings2.customitem.itemlistgui

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

// 아이템들 확인하고 꺼낼 수도 있는 GUI를 여는 명령어
object CustomItemListCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return true
        val customItemListGUI = CustomItemListGUI(sender)
        Bukkit.getPluginManager().registerEvents(customItemListGUI, plugin)
        return true
    }
}