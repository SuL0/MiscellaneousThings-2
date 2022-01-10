package kr.sul.miscellaneousthings2.tutorial

import com.destroystokyo.paper.Title
import com.github.shynixn.mccoroutine.launch
import kotlinx.coroutines.delay
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.miscellaneousthings2.tutorial.messagehelper.FullMessage
import kr.sul.miscellaneousthings2.tutorial.messagehelper.KeepMessageInChat
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.time.Duration


// DB에 튜토리얼 수행했는지 저장

// 채팅으로 하는 편이 좋아보이는데?
// 같은 내용으로 채팅을 계속 보냄 -> 채팅이 내려가지 않게끔
// TODO 중간에 플레이어 나가는 것에 대한 대비책
class TutorialPlayer(private val p: Player): Listener {
    private val keepMessageInChat = KeepMessageInChat(p)
    private var onYes: () -> Any = {}  // 1번 쓰면 초기화됨
    private var onNo: () -> Any = {}   // 1번 쓰면 초기화됨

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        step1()
    }

    private fun step1() {
        plugin.launch {
            val fullMsg = FullMessage(3)
                .add("          §e마인크래프트에 숙달되었거나 유사 장르를 해보셨습니까?")
            fullMsg.send(p)
            delay(3*20*50L)

            fullMsg.addYesOrNo()
            keepMessageInChat.keepThisMessage(fullMsg)
        }
        onYes = {
            step2()
            keepMessageInChat.keepNothing()
        }
        onNo = {
            p.kickPlayer("") // TODO
            keepMessageInChat.destroy()
        }
    }

    private fun step2() {
        plugin.launch {
            val fullMsg2 = FullMessage(3)
                .add("        §f지금부터 플레이에 도움이 될만한걸 알려드릴 생각입니다.")
            fullMsg2.send(p)
            delay(3*20*50L)

            fullMsg2.add("")
            fullMsg2.add("        §f그렇지만 이것을 볼 것인지, 안 볼 것인지는 자유입니다.")
            fullMsg2.send(p)
            delay(3*20*50L)

            fullMsg2.addYesOrNo()
            keepMessageInChat.keepThisMessage(fullMsg2)
        }
        onYes = {
            keepMessageInChat.destroy()
            keepMessageInChat.clearChat()
            step3()
        }
        onNo = {
            p.sendTitle(Title("", "§f명령어 §e'/튜토리얼' §f을 통해 언제든 튜토리얼을 볼 수 있습니다."))
            destroy()
        }
    }

    private fun step3() {
        plugin.launch {
            FullMessage(4)
                .add("        §f이것은 플레이에 도움을 줄 뿐이니, 완벽히 알 필요는 없습니다.")
                .send(p)

            val fullMsg1 = FullMessage(2)
                .add("        §4§l미지의 바이러스")
            fullMsg1.send(p)
            delay(6*20*50L)
            fullMsg1.add("").add("        §f유래없는 최악의 바이러스에 의해").send(p)
            delay(4*20*50L)
            fullMsg1.add("").add("        §f인류는 위험에 빠졌습니다.").send(p)
            delay(6*20*50L)

            val fullMsg2 = FullMessage(2)
                .add("        §2§l생존")
            fullMsg2.send(p)
            delay(6*20*50L)
            fullMsg2.add("").add("        §f여러분들은 §2§l숲 §f또는 §9§l바다 §f또는 §4§l도시§f에서 미지로부터 살아 남아야 합니다.").send(p)
            delay(6*20*50L)

            FullMessage(2)
                .add("        §2§l숲. ")
                .send(p)
            delay(2*20*50L)
            FullMessage(2)
                .add("        §2§l숲. §9§l바다.")
                .send(p)
            delay(2*20*50L)
            val fullMsg3 = FullMessage(2)
                .add("        §2§l숲. §9§l바다. §4§l도시.")
            fullMsg3.send(p)
            delay(3*20*50L)

            fullMsg3.add("").add("        §2§l숲§f은 식량이 풍부하며").send(p)
            delay(4*20*50L)

            fullMsg3.add("").add("        §9§l바다§f는 외부로부터 안전하며").send(p)
            delay(4*20*50L)

            fullMsg3.add("").add("        §4§l도시§f는 풍부한 물자가 항상 존재합니다.").send(p)
            delay(6*20*50L)


            val fullMsg4 = FullMessage(2)
                .add("        §4§l알 수 없는 망자들")
            fullMsg4.send(p)
            delay(4*20*50L)

            fullMsg4.add("").add("        §f어딜가던 그들은 나타나 항상 적대적일 것입니다.").send(p)
            delay(6*20*50L)

            val fullMsg5 = FullMessage(2)
                .add("        §2§l협력")
            fullMsg5.send(p)
            delay(4*20*50L)
            fullMsg5.add("").add("        §f혼자보다 여럿이 행동하는게 생존에 유리할 것입니다.").send(p)
            delay(4*20*50L)
            fullMsg5.add("").add("        §2§l길드§f나 §9§l파티기능§f이 존재합니다.").send(p)
            delay(6*20*50L)

            val fullMsg6 = FullMessage(2)
                .add("        §4§l약탈")
            fullMsg6.send(p)
            delay(4*20*50L)
            fullMsg6.add("").add("        §f사람들은 협력자가 되어줄 수도 있지만, 반대가 되는 경우도 있습니다.").send(p)
            delay(4*20*50L)
            fullMsg6.add("").add("        §f스스로 지킬 수 있도록 강해져야 합니다.").send(p)
            delay(8*20*50L)
            step4()
        }
    }
    fun step4() {
        plugin.launch {
            val fullMsg1 = FullMessage(2)
                .add("        §f§l3단계 : 학습시작")
            fullMsg1.send(p)
            delay(6*20*50L)
            fullMsg1.add("").add("        §f지혈방법 , 갈증관리")
            // 이 다음부터 채팅으로 가야하나?
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onYes(e: PlayerCommandPreprocessEvent) {
        if (e.isCancelled || e.player != p) return

        // TODO 다른 플러그인과 마찰을 일으킬 수 있으니, /(Random String) 으로 하는 것이 낫겠다.
        if (e.message == "/yes") {
            e.isCancelled = true
            val copiedOnYes = onYes
            // TODO
//            onYes = {}
//            onNo = {}
            copiedOnYes.invoke()
        } else if (e.message == "/no") {
            e.isCancelled = true
            val copiedOnNo = onNo
//            onYes = {}
//            onNo = {}
            copiedOnNo.invoke()
        }
    }


    private fun FullMessage.addYesOrNo() {
        add("")
        add("")
        val jsonMsg = arrayListOf<TextComponent>().run {
            add(TextComponent("               "))
            add(TextComponent("§a§l§n[   예   ]").run {
                clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yes")
                this
            })
            add(TextComponent("     "))
            add(TextComponent("§c§l§n[  아니오  ]").run {
                clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/no")
                this
            })
            this
        }
        add(jsonMsg)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        if (e.player == p) {
            destroy()
        }
    }
    private fun destroy() {
        HandlerList.unregisterAll(this)
        keepMessageInChat.destroy()
    }
}