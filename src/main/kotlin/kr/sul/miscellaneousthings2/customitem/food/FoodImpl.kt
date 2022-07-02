package kr.sul.miscellaneousthings2.customitem.food

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

                if (e.player.inventory.itemInMainHand.amount == 1) {
                    e.player.inventory.itemInMainHand = null
                } else {
                    e.player.inventory.itemInMainHand.amount -= 1
                    // 아래 없어도 amount 줄어드는 거 업데이트는 잘 되는데,
                    // 중요한 역할은 2번째 아이템을 갉아먹을 때 계속 갉아먹기만 하고 정작 아이템은 먹히질 않는 문제를 해결해 줌 (이유는 모르겠는데, SET_SLOT 패킷 똑같이 protocolLib으로 보내줘도 얘는 안됨)
                    e.player.inventory.itemInMainHand = e.player.inventory.itemInMainHand
                }
                if (e.player.health + (find.addFoodLevel/4) <= 20) {
                    e.player.health += (find.addFoodLevel/4)  // 1이 반 칸
                } else {
                    e.player.health = 20.0
                }
                e.player.foodLevel += find.addFoodLevel  // 1이 반 칸
            }
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