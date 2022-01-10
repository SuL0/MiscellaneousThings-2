package kr.sul.miscellaneousthings2.tutorial.messagehelper

import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

/**
 * 채팅창을 가득 채우는 메세지
 * @param insertingPointOfMessage 어느 줄에서부터 메세지를 넣기 시작할것인가? (공백 채워넣어야 하기 때문) (채팅창은 총 20줄)
 */
class FullMessage(val insertingPointOfMessage: Int) {
    val originalMsgList = arrayListOf<Any>()
    private var isProcessed = false
    private val processedMsgList = arrayListOf<Any>()

    fun add(strMsg: String): FullMessage {
        if (isProcessed) {
            isProcessed = false
        }
        originalMsgList.add(strMsg)
        return this
    }
    fun add(jsonMsg: List<TextComponent>): FullMessage {
        if (isProcessed) {
            isProcessed = false
        }
        originalMsgList.add(JsonMsg(jsonMsg))
        return this
    }

    fun send(p: Player) {
        if (!isProcessed) {
            process()
        }
        for (msg in processedMsgList) {
            if (msg is String) {
                p.sendMessage(msg)
            } else if (msg is JsonMsg) {
                msg.send(p)
            }
        }
    }

    // 가공처리
    private fun process() {
        isProcessed = true
        for (i in 1..11) {
            processedMsgList.add("")
        }
        for (i in 1..10) {
            if (i == insertingPointOfMessage) {
                processedMsgList.addAll(originalMsgList)
            }
            if (i in insertingPointOfMessage..insertingPointOfMessage+originalMsgList.size) {
                continue
            }
            processedMsgList.add("")
        }
    }




    private class JsonMsg(private val msg: List<TextComponent>) {
        fun send(p: Player) {
            p.sendMessage(*msg.toTypedArray())
        }
    }
}