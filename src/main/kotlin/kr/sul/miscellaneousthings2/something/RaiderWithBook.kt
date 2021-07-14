package kr.sul.miscellaneousthings2.something

import kr.sul.servercore.util.ItemBuilder.nameIB
import net.md_5.bungee.api.chat.BaseComponent
import org.apache.commons.lang.StringUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import xyz.upperlevel.spigot.book.BookUtil
import kotlin.math.round

object RaiderWithBook: Listener {
    private val BOOK_MATERIAL = Material.COCOA
    private val bookItem: ItemStack
        get() = ItemStack(BOOK_MATERIAL).nameIB("§c[ §f레이더 §c]")

    private const val NEAR_DISTANCE = 50
    private const val FAR_DISTANCE = 100
    private const val LINES_PER_PAGE = 13


    private fun openRaiderBook(p: Player) {
        // TODO p.world.entities 수정
        val detectedPlayers = p.world.entities.filter { it.location.distance(p.location) <= FAR_DISTANCE && it != p }
            .sortedBy { it.location.distance(p.location) }

        val book = BookUtil.writtenBook()
        val tempPageStorage = arrayListOf<Array<BaseComponent>>()
        var pageBuilder: BookUtil.PageBuilder? = null
        for ((cnt, detectedPlayer) in detectedPlayers.withIndex()) {
            if (cnt % LINES_PER_PAGE == 0) {
                if (cnt != 0) {  // cnt == 0은 예외
                    tempPageStorage.add(pageBuilder!!.build())
                }
                pageBuilder = BookUtil.PageBuilder()  //
                pageBuilder.add("§4§l플레이어  거리  소속길드").newLine()
            }
            val name = StringUtils.rightPad(detectedPlayer.type.name, 10, ' ')
            val distance = round(detectedPlayer.location.distance(p.location)*10)/10
            if (distance > NEAR_DISTANCE && distance <= FAR_DISTANCE) {  // 50~100칸   (&k 로 인해서 거리가 제대로 보이지 않음)
                pageBuilder!!.add("$name §k[${distance}]").newLine()
            } else {  // 50칸 이내
                pageBuilder!!.add("$name ${distance}m").newLine()
//                GlowAPI.setGlowing(detectedPlayer, GlowAPI.Color.RED, p)   // TODO <= 50인 엔티티 3초간 글로우
            }
        }
        val bookItem = book.author("").title("").pages(tempPageStorage).build()
        BookUtil.openPlayer(p, bookItem)
    }





    // BOOK 핫바 9번 슬롯에 고정
    @EventHandler
    fun onRightClickBook(e: PlayerInteractEvent) {
        if (e.player.inventory.itemInMainHand.type == BOOK_MATERIAL) {
            e.isCancelled = true
            openRaiderBook(e.player)
        }
    }
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val p = e.player
        val pInv = e.player.inventory
        if (pInv.getItem(8) != null && pInv.getItem(8).type != BOOK_MATERIAL) {  // 만약 8번 슬롯에 무언가 아이템이 있는 경우, 해당 아이템을 옮겨줌
            val itemToMove = pInv.getItem(8)
            if (pInv.firstEmpty() != -1) {  // 인벤이 꽉차지 않았을 때
                pInv.addItem(itemToMove)
            } else {
                p.world.dropItem(p.location, itemToMove)
            }
        }
        pInv.setItem(8, bookItem)
    }
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
        if (e.itemDrop.itemStack.type == BOOK_MATERIAL) {
            e.isCancelled = true
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    fun onSwap(e: PlayerSwapHandItemsEvent) {
        if (e.isCancelled) return
        if (e.offHandItem.type == BOOK_MATERIAL) {
            e.isCancelled = true
        }
    }
}