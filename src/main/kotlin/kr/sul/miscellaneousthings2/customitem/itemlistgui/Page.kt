package kr.sul.miscellaneousthings2.customitem.itemlistgui

import kr.sul.miscellaneousthings2.customitem.enums.NormalItem
import org.bukkit.inventory.Inventory

class Page(
    private val name: String,
    private val enumValues: Array<NormalItem>
) {
    // 현재 인벤에 해당 페이지로 구성
    fun organize(inv: Inventory) {

    }


    companion object {
        // 네비바 같은 공통적인 것 구성
        fun organizeDefaultThings(inv: Inventory) {

        }
    }
}