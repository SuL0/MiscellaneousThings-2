package kr.sul.miscellaneousthings2.customitem.food

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack

object FoodImpl: Listener {
    @EventHandler
    fun onTest(e: PlayerCommandPreprocessEvent) {
        if (e.message == "/hungry" && e.player.isOp) {
            e.isCancelled = true
            e.player.health = 2.0
            e.player.foodLevel = 2
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onEatFood(e: PlayerItemConsumeEvent) {
        if (e.isCancelled) return

        if (e.player.inventory.itemInMainHand.type.isEdible) {
//            val find = FoodDefinedMgr.findMatching(e.item)  // e.item이 ItemStack을 clone 하는 바람에 ItemStack이 CraftItemStack이 아니게 됨
            val find = FoodDefinedMgr.findMatching(e.player.inventory.itemInMainHand)

            if (find != null) {
                e.isCancelled = true
                find.consume(e.player)
            }
        }

        Bukkit.getScheduler().runTask(plugin) {
            if (e.player.foodLevel >= 20) {
                e.player.foodLevel = 19
            }
        }
    }

    private fun FoodDefined.consume(p: Player) {
        if (p.inventory.itemInMainHand.amount == 1) {
            p.inventory.itemInMainHand = null
        } else {
            p.inventory.itemInMainHand.amount -= 1
            // 아래 없어도 amount 줄어드는 거 업데이트는 잘 되는데,
            // 중요한 역할은 2번째 아이템을 갉아먹을 때 계속 갉아먹기만 하고 정작 아이템은 먹히질 않는 문제를 해결해 줌 (이유는 모르겠는데, SET_SLOT 패킷 똑같이 protocolLib으로 보내줘도 얘는 안됨)
            p.inventory.itemInMainHand = p.inventory.itemInMainHand
        }

        if (p.health + addHealthLevel <= p.getAttribute(Attribute.GENERIC_MAX_HEALTH).value) {
            p.health += addHealthLevel  // 1이 반 칸
        } else {
            p.health = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).value
        }

        if (p.foodLevel + this.addFoodLevel < 20) {
            p.foodLevel += this.addFoodLevel  // 1이 반 칸
        } else {
            p.foodLevel = 19
        }
    }


    // 왜인지 몰라도 둘 다 안됨, inventory.setSlot()은 SET_SLOT 패킷을 보냄
    fun sendFakeItemInMainHandPacket(p: Player, item: ItemStack) {
        val entityP = (p as CraftPlayer).handle
//        pm.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT).run {
//            integers.write(0, p.entityId)
//            itemSlots.write(0, EnumWrappers.ItemSlot.MAINHAND)
//            itemModifier.write(0, item) // heldItem
//            pm.sendServerPacket(p, this)
//        }
//        pm.createPacket(PacketType.Play.Server.SET_SLOT).run {
//            integers.write(0, p.handle.defaultContainer.windowId)
//            integers.write(1, InventorySlotConverterUtil.spigotSlotToNmsSlot(p.inventory.heldItemSlot))
//            itemModifier.write(0, item)
//            pm.sendServerPacket(p, this)
//        }
    }
}