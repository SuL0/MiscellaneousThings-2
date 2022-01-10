package kr.sul.miscellaneousthings2.something

import kr.sul.servercore.util.ItemBuilder.durabilityIB
import kr.sul.servercore.util.ItemBuilder.nameIB
import kr.sul.servercore.util.PlayerDistance.distance
import net.md_5.bungee.api.chat.BaseComponent
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.inventivetalent.glow.GlowAPI
import xyz.upperlevel.spigot.book.BookUtil
import xyz.upperlevel.spigot.book.BookUtil.PageBuilder
import java.util.*
import kotlin.math.round


object RaiderWithBook: Listener {
    private val bookItem: ItemStack
        get() = ItemStack(Material.INK_SACK).durabilityIB(3).nameIB("§c[ §f레이더 §c]")
    private const val NEAR_DISTANCE = 50
    private const val FAR_DISTANCE = 100
    private const val LINES_PER_PAGE = 13

    private val glow = hashMapOf<Player, ArrayList<UUID>>()  // Key가 보고 있는 글로우 상대를 Value에 저장

    private fun openRaiderBook(p: Player) {
        val detectedPlayers = p.world.players.filter { it.distance(p) <= FAR_DISTANCE && it != p }
            .sortedBy { it.location.distance(p.location) }

        if (glow.containsKey(p)) {
            removeUselessGlow(p, detectedPlayers)
        }

        val book = BookUtil.writtenBook()
        val tempPageStorage = arrayListOf<Array<BaseComponent>>()
        var pageBuilder: PageBuilder? = null
        for ((cnt, detectedPlayer) in detectedPlayers.withIndex()) {
            if (cnt % LINES_PER_PAGE == 0) {
                if (cnt != 0) {  // cnt == 0은 예외
                    tempPageStorage.add(pageBuilder!!.build())
                }
                pageBuilder = PageBuilder()  //
                pageBuilder.add("§4§l플레이어  거리  소속길드").newLine()
            }
            val name = StringUtils.rightPad(detectedPlayer.type.name, 10, ' ')
            val distance = round(detectedPlayer.distance(p)*10)/10
            if (distance > NEAR_DISTANCE && distance <= FAR_DISTANCE) {  // 50~100칸   (&k 로 인해서 거리가 제대로 보이지 않음)
                pageBuilder!!.add("$name §k[${distance}]").newLine()
            } else {  // 50칸 이내
                pageBuilder!!.add("$name ${distance}m").newLine()
                glowDetectedPlayer(p, detectedPlayer as Player)
            }
        }
        val bookItem = book.author("").title("").pages(tempPageStorage).build()
        BookUtil.openPlayer(p, bookItem)
    }


    // glow[p] (List<UUID>) 에서 playersToStay 에 포함되지 않는 값은 모두 글로우 해제하고 값 제거
    private fun removeUselessGlow(p: Player, playersToStay: List<Player>) {
        val playersToStayUUID = playersToStay.map { it.uniqueId }
        val playersToRemoveUUID = glow[p]!!.filter { !playersToStayUUID.contains(it) }
        for (pToRemoveUUID in playersToRemoveUUID) {
            val pToRemove = Bukkit.getPlayer(pToRemoveUUID)
                ?: continue  // pToRemove가 Offline 유저일 때
            GlowAPI.setGlowing(pToRemove, false, p)
        }
        glow[p]!!.removeAll(playersToRemoveUUID)
    }

    private fun glowDetectedPlayer(p: Player, detectedPlayer: Player) {
        if (!glow.containsKey(p)) {
            glow[p] = arrayListOf()
        }
        if (!glow[p]!!.contains(detectedPlayer.uniqueId)) {  // 만약 이미 Glow가 된 상태라면 다시 Glow를 시키지는 않음
            glow[p]!!.add(detectedPlayer.uniqueId)
            GlowAPI.setGlowing(detectedPlayer, GlowAPI.Color.DARK_PURPLE, p)
        }
    }




    // BOOK 핫바 9번 슬롯에 고정
    @EventHandler
    fun onRightClickBook(e: PlayerInteractEvent) {
        if (e.player.inventory.itemInMainHand.isSimilar(bookItem)) {
            e.isCancelled = true
            openRaiderBook(e.player)
        }
    }


    // 인벤 9번에 나침반 주는 것 비활성화
//    @EventHandler
//    fun onJoin(e: PlayerJoinEvent) {
//        val p = e.player
//        val pInv = e.player.inventory
//        if (pInv.getItem(8) != null && !pInv.getItem(8).isSimilar(bookItem)) {  // 만약 8번 슬롯에 무언가 아이템이 있는 경우, 해당 아이템을 옮겨줌
//            val itemToMove = pInv.getItem(8)
//            if (pInv.firstEmpty() != -1) {  // 인벤이 꽉차지 않았을 때
//                pInv.addItem(itemToMove)
//            } else {
//                p.world.dropItem(p.location, itemToMove)
//            }
//        }
//        pInv.setItem(8, bookItem)
//    }
    @EventHandler(priority = EventPriority.LOW)
    fun onInvClick(e: InventoryClickEvent) {
        if (e.isCancelled) return
        if (e.clickedInventory is PlayerInventory && e.slot == 8) {
            e.isCancelled = true
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    fun onDrop(e: PlayerDropItemEvent) {
        if (e.isCancelled) return
        if (e.itemDrop.itemStack.isSimilar(bookItem)) {
            e.isCancelled = true
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    fun onSwap(e: PlayerSwapHandItemsEvent) {
        if (e.isCancelled) return
        if (e.offHandItem.isSimilar(bookItem)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        glow.remove(e.player)
    }
}