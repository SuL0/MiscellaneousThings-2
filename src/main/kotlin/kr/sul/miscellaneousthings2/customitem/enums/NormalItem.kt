package kr.sul.miscellaneousthings2.customitem.enums

import kr.sul.miscellaneousthings2.customitem.util.ItemStackBasicInfo

interface NormalItem {
    val enumUuid: String  // ItemStack과 enum value를 잇기 위한 uuid
    val basicInfo: ItemStackBasicInfo
}