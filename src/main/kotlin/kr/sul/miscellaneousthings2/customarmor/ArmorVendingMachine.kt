package kr.sul.miscellaneousthings2.customarmor

import com.github.shynixn.mccoroutine.launch
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTItem
import kotlinx.coroutines.delay
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.miscellaneousthings2.customarmor.NbtDurabilityArmor.Companion.CURRENT_DURABILITY_LORE
import kr.sul.miscellaneousthings2.customarmor.NbtDurabilityArmor.Companion.MAX_DURABILITY_LORE
import kr.sul.servercore.util.ItemBuilder.nameIB
import kr.sul.servercore.util.MsgPrefix
import net.minecraft.server.v1_12_R1.ItemArmor
import net.minecraft.server.v1_12_R1.NBTTagCompound
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


// 내구도는 어떻게 하지?
object ArmorVendingMachine: CommandExecutor {
    private val PREFIX = MsgPrefix.get("ARMOR")
    private val NBTItemCommand = CommandNBTItem()
    private const val NBT_DEFENSE_KEY = "MiscellaneousThings.ArmorDefense"
    private const val NBT_SPEED_KEY = "MiscellaneousThings.ArmorSpeed"


    // 방어구 수정
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return true
        val item = sender.inventory.itemInMainHand
        if (item == null) {
            sender.sendMessage("${PREFIX}방어구 아이템을 들어 주십시오.")
        }
        if (CraftItemStack.asNMSCopy(item).item !is ItemArmor) {
            sender.sendMessage("${PREFIX}해당 아이템은 방어구가 아닙니다.")
            return true
        }
        if (args.isEmpty()) {
            sender.sendMessage("${PREFIX}/${label} <이름> <방어력> <내구성> <이동속도> : 대상 방어구(lore에 $CURRENT_DURABILITY_LORE, $MAX_DURABILITY_LORE 포함)를 들은 상태로 해당 명령어 입력")
            return true
        }

        val name = args[0]
        val armorValue = args[1].toFloat()
        val durability = args[2].toInt()
        val speed = args[3].toFloat()

        NBTItemCommand.mod_delallCommand(sender, arrayListOf("").toTypedArray())
        // TODO 1틱 기다리는 것을 없애도 되나? (mod_delallCommand 때문)
        plugin.launch {
            delay(1) // 알아서 1틱만 쉼
            if (!sender.isOnline) return@launch
//            Bukkit.broadcastMessage("${item.type} != ${sender.inventory.itemInMainHand}")
            if (item.type != sender.inventory.itemInMainHand.type) {
                sender.sendMessage("${PREFIX}§c1틱 전의 아이템 타입과 현재의 아이템 타입이 다릅니다.")
                return@launch
            }
            val item = sender.inventory.itemInMainHand
            vendingMachine(sender, item, name, armorValue, durability, speed)  // TODO clone해서 item 넣고 vendingMachine은 ItemStack 내보내주는 게 좋을 듯
        }
        return true
    }




    // 이름은 vendingMachine인데 정작 parameter로 받은 아이템을 수정해서 내보냄
    private fun vendingMachine(p: Player?, armorItem: ItemStack, name: String, defense: Float, durability: Int, speed: Float) {
        if (!NbtDurabilityArmor.initSetting(armorItem, durability, durability)) {
            p?.sendMessage("${PREFIX}§c아이템의 lore에서 $CURRENT_DURABILITY_LORE 또는 $MAX_DURABILITY_LORE 이 발견되지 않습니다. §4§l[경고]")
        }
        armorItem.nameIB(name)
        val tag = (armorItem as CraftItemStack).handle.tagOrDefault
        setDefenseNBT(tag, defense)
        setSpeedNBT(tag, speed)
    }

    private fun setDefenseNBT(tag: NBTTagCompound, value: Float) {
        tag.setFloat(NBT_DEFENSE_KEY, value)
    }
    fun getDefenseNBT(tag: NBTTagCompound): Float {
        return tag.getFloat(NBT_DEFENSE_KEY)
    }
    private fun setSpeedNBT(tag: NBTTagCompound, value: Float) {
        tag.setFloat(NBT_SPEED_KEY, value)
    }
    fun getSpeedNBT(tag: NBTTagCompound): Float {
        return tag.getFloat(NBT_SPEED_KEY)
    }
}