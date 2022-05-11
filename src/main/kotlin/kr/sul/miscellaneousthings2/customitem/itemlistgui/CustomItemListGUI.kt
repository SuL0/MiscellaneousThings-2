package kr.sul.miscellaneousthings2.customitem.itemlistgui

import kr.sul.miscellaneousthings2.customitem.CustomItemMain
import kr.sul.miscellaneousthings2.customitem.enums.NormalItem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

// 페이지 넘겨서 카테고리 전환
class CustomItemListGUI(
        private val p: Player
    ): Listener {
    private val inv = Bukkit.createInventory(null, 9*6, "Custom Item List GUI")
    private val currentPage = pages.first()

    init {
        p.openInventory(inv)
        switchPage(currentPage)
    }


    private fun switchPage(page: Page) {

    }

    // 아이템 가져오기
    @EventHandler(priority = EventPriority.HIGH)
    fun onClick(e: InventoryClickEvent) {
        if (e.isCancelled) return

    }







    @EventHandler(priority = EventPriority.LOW)
    fun onInvClose(e: InventoryCloseEvent) {
        if (e.player != p) return
        destroy()
    }
    private fun destroy() {
        HandlerList.unregisterAll(this)
    }

    companion object {
        private val pages = arrayListOf<Page>()

        init {
            val enumClasses = CustomItemMain.customItemCategories.map { it.customItemDefineClass }
            for (enumClass in enumClasses) {
                pages.add(
                    Page(
                        enumClass.toString(),
                        enumClass.enumConstants as Array<NormalItem>
                    )
                )
            }
        }
    }
}