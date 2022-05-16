package kr.sul.miscellaneousthings2.customitem.itemlistgui

import kr.sul.miscellaneousthings2.customitem.CustomItemMain
import kr.sul.miscellaneousthings2.customitem.forenum.CustomItemDefinedMgr
import kr.sul.miscellaneousthings2.customitem.forenum.NormalItem

// 미리 생성된 페이지 (CusomItemListGUI가 현재 페이지가 아닌 페이지 객체를 미리 생성해둬야 하는 것을 최적화 하기 위해)
class SamplePage(
    val name: String,
    val customItemDefined: Array<NormalItem>,
    val customItemDefinedMgr: CustomItemDefinedMgr<NormalItem>
) {

    companion object {
        val list = arrayListOf<SamplePage>()
        init {
            for (customItemCategory in CustomItemMain.customItemCategories) {
                list.add(
                    SamplePage(
                        customItemCategory.customItemDefinedClass.simpleName,
                        customItemCategory.customItemDefinedClass.enumConstants as Array<NormalItem>,
                        customItemCategory.customItemDefinedMgr
                    )
                )
            }
        }
    }
}



