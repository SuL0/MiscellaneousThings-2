package kr.sul.miscellaneousthings2.warpgui.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class PlayerData(val uuidStr: String,
                      var haveEverPlayedOnNormal: Boolean = false,
                      var canPlayOnHard: Boolean = false) {
//    @Transient val p: Player = Bukkit.getPlayer(UUID.fromString(uuidStr))  // Transient 아니어도 어차피 p는 default value에서 벗어날 일 없기에 JSON에 저장 안 되긴 함

    fun toJsonString(): String {
        return Json.encodeToString(this)
    }
    companion object {
        fun fromJsonString(jsonStr: String): PlayerData {
            return Json.decodeFromString(jsonStr)
        }
    }
}