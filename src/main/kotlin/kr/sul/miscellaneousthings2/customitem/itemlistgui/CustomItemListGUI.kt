package kr.sul.miscellaneousthings2.customitem.itemlistgui

import kr.sul.miscellaneousthings2.customitem.itemlistgui.button.SwitchCategoryButton
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

// 최상위 GUI 클래스
class CustomItemListGUI(
    private val p: Player
): Listener {
    private val inv = Bukkit.createInventory(null, 9*6, "Custom Item List GUI")
    private var currentPage = Page(p, inv, SamplePage.list.first())
    private val switchCategoryButtons = arrayListOf<SwitchCategoryButton>()

    init {
        p.openInventory(inv)
        switchCategoryButtons.addAll(
            SamplePage.list.mapIndexed { i, samplePage ->
                SwitchCategoryButton(i, samplePage)
            }
        )
        switchPage(currentPage)
    }


    private fun switchPage(page: Page) {
        currentPage = page
        inv.clear()
        // 카테고리 전환 버튼
        switchCategoryButtons.forEach { button ->
            button.place(inv)
        }
        page.organize(inv, 1, false)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onClick(e: InventoryClickEvent) {
        if (e.isCancelled || e.whoClicked != p
            || e.currentItem == null || e.clickedInventory != inv) return
        e.isCancelled = true

        // 카테고리 전환 버튼
        switchCategoryButtons.find { it.whatSlotToAllocate == e.slot }?.let { switchCategoryButton ->
            switchPage(Page(p, inv, switchCategoryButton.matchingSamplePage))
            return
        }
        currentPage.onClick(e)
    }







    @EventHandler(priority = EventPriority.LOW)
    fun onInvClose(e: InventoryCloseEvent) {
        if (e.player != p) return
        destroy()
    }
    private fun destroy() {
        HandlerList.unregisterAll(this)
    }
}