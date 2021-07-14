package kr.sul.miscellaneousthings2.something.block

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.PlayerInventory

object BlockLeftHand: Listener {
    private const val INV_SLOT = 40

    @EventHandler(priority = EventPriority.LOW)
    fun onSwap(e: PlayerSwapHandItemsEvent) {
        e.isCancelled = true
    }
    @EventHandler(priority = EventPriority.LOW)
    fun onClickLeftHandSlot(e: InventoryClickEvent) {
        if (e.clickedInventory is PlayerInventory && e.slot == INV_SLOT) {
            e.isCancelled = true
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    fun onInvDrag(e: InventoryDragEvent) {
        if (e.inventorySlots.contains(INV_SLOT)) {
            e.isCancelled = true
        }
    }
}