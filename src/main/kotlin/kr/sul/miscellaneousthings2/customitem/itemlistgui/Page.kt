package kr.sul.miscellaneousthings2.customitem.itemlistgui

import kr.sul.miscellaneousthings2.customitem.itemlistgui.button.InnerPageButton
import kr.sul.servercore.util.MsgPrefix
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import kotlin.math.min

// 한 가지 특정 카테고리의 GUI 페이지
// innerPage가 있으려면 공용 객체가 아니라 개인만의 객체여야하는데?
// pages를 companion에서 만들어놓고 clone해서 써야하나?

class Page(
    private val p: Player,
    private val inv: Inventory,
    samplePage: SamplePage
) {
    private val customItemDefined = samplePage.customItemDefined
    private val customItemDefinedMgr = samplePage.customItemDefinedMgr
    private var currentInnerPageNum = 1  // 페이지 안의 아이템이 4줄밖에 못 넣음으로 아이템 넘기는 페이지

    // 현재 인벤에 해당 페이지로 구성
    fun organize(inv: Inventory, innerPage: Int=1, bClearInv: Boolean = true) {
        if (bClearInv) {
            inv.clear()
        }
        InnerPageButton.values().forEach { button ->
            button.place(inv)
        }
        // 실질적 내용인 아이템
        for (i in 0 until min(INNER_PAGE_LINES*9, customItemDefined.size-((innerPage-1)*INNER_PAGE_LINES))) {
            inv.setItem(i+9, customItemDefinedMgr.makeNewItemStack(customItemDefined[((innerPage-1)*INNER_PAGE_LINES)+i]))
        }
    }



    private fun toPreviousInnerPage() {
        if (currentInnerPageNum == 1) {
            p.sendMessage("${MsgPrefix.get("CUSTOM ITEM")}이전 페이지가 존재하지 않습니다.")
            return
        }
        organize(inv, currentInnerPageNum-1)
    }
    private fun toNextInnerPage() {
        if (customItemDefined.size <= currentInnerPageNum*INNER_PAGE_LINES*9) {
            p.sendMessage("${MsgPrefix.get("CUSTOM ITEM")}다음 페이지가 존재하지 않습니다.")
            return
        }
        organize(inv, currentInnerPageNum+1)
    }




    // 클릭한 아이템을 플레이어에게 주기
    fun onClick(e: InventoryClickEvent) {
        // NavBar인지 확인
        when (e.slot) {
            InnerPageButton.PREVIOUS_BUTTON.whatSlotToAllocate -> {
                toPreviousInnerPage()
            }
            InnerPageButton.NEXT_BUTTON.whatSlotToAllocate -> {
                toNextInnerPage()
            }
            else -> {
                (e.whoClicked as Player).sendMessage("${MsgPrefix.get("CUSTOM ITEM")}아이템이 복사되었습니다.")
                (e.whoClicked as Player).inventory.addItem(e.currentItem)
            }
        }
    }

    companion object {
        private const val INNER_PAGE_LINES = 4  // 아이템을 두는 innerPage가 할애할 GUI의 줄 수
    }
}