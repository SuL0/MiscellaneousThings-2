package kr.sul.miscellaneousthings2.customitem.enums

import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import java.util.function.Function
import java.util.stream.Collectors

// out: 공변성(variance. extends), in: 반공변성(invariance. super)  -> 리스코프 치환 법칙       - 참고용으로 적었고 여기서 쓸 이유 없음
// CustomItem의 enum value와 실제 ItemStack을 잇기 위한 UUID
open class CustomItemDefinedUuidMgr<T: NormalItem>(private val nameOfEnumCategory: String, enumValues: Array<T>) {
    companion object {
        const val ENUM_UUID_KEY = "Misc-EnumCustomItem UUID"
        const val ENUM_CATEGORY_KEY = "Misc-EnumCustomItem Category"
    }

    // https://pjh3749.tistory.com/279
    private val maps = enumValues.asList().stream()
            .collect(Collectors.toMap(NormalItem::enumUuid, Function.identity()))

    fun carveEnumUuid(item: CraftItemStack, enumItem: T) {
        item.handle.tagOrDefault.setString(ENUM_UUID_KEY, enumItem.enumUuid)
        item.handle.tagOrDefault.setString(ENUM_CATEGORY_KEY, nameOfEnumCategory)
    }
    fun findMatching(item: ItemStack): T? {
        if (item.type == Material.AIR || item !is CraftItemStack || item.handle == null) return null
        val enumUuid = item.handle.tag?.getString(ENUM_UUID_KEY) ?: return null
        return maps[enumUuid]
    }
}