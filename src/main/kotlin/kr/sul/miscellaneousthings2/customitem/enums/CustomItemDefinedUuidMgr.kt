package kr.sul.miscellaneousthings2.customitem.enums

import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import java.util.function.Function
import java.util.stream.Collectors

// out: 공변성(variance), in: 반공변성(invariance)  -> 리스코프 치환 법칙       - 참고용으로 적었고 여기서 쓸 이유 없음
// CustomItem의 enum value와 실제 ItemStack을 잇기 위한 UUID
open class CustomItemDefinedUuidMgr<T: NormalItem>(nameOfEnumType: String, enumValues: Array<T>) {
    private val enumUuidKey = "Misc-Enum$nameOfEnumType UUID"
    private val maps = enumValues.asList().stream()
            .collect(Collectors.toMap(NormalItem::enumUuid, Function.identity()))  // TODO 테스트
//    private val maps = hashMapOf<String, T>().run {
//        for (value in enumValues) {
//            put(value.enumUuid, value)
//        }
//        this
//    }

    fun carveEnumUuid(item: CraftItemStack, enumItem: T) {
        item.handle.tagOrDefault.setString(enumUuidKey, enumItem.enumUuid)
    }
    fun findMatching(item: ItemStack): T? {
        if (item.type == Material.AIR || item !is CraftItemStack || item.handle == null) return null
        val enumUuid = item.handle.tag?.getString(enumUuidKey) ?: return null
        return maps[enumUuid]
    }
}