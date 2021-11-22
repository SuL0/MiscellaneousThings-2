package kr.sul.miscellaneousthings2.something

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.ClassifyWorlds
import kr.sul.servercore.util.ItemBuilder.durabilityIB
import kr.sul.servercore.util.ItemBuilder.loreIB
import kr.sul.servercore.util.ItemBuilder.nameIB
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

/**
 * 명령어 /나침반 : 나침반 지급
 */
object WarpWithCompass: Listener, CommandExecutor {
    private val COMPASS_ITEM = ItemStack(Material.COMPASS).nameIB("§6§l워프 도우미").loreIB("&f해당 아이템을 통해 스폰의 각 장소에 워프할 수 있습니다.", 2)

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }
        val p = sender as Player
        val pInv = p.inventory
        if (!ClassifyWorlds.isSpawnWorld(p.world)) {
            p.sendMessage("§f§lCOMPASS §7::  §7현재 스폰 월드에 위치하고 있지 않습니다.")
            return true
        }

        if (pInv.itemInMainHand?.type != Material.AIR && !pInv.itemInMainHand.isSimilar(COMPASS_ITEM)) {  // 만약 8번 슬롯에 무언가 아이템이 있는 경우, 해당 아이템을 옮겨줌
            val itemToMove = pInv.itemInMainHand
            if (pInv.firstEmpty() != -1) {  // 인벤이 꽉 차지 않았을 때
                pInv.addItem(itemToMove)
            } else {
                p.sendMessage("§f§lCOMPASS §7::  §c인벤토리가 꽉 차 있습니다.")
                return true
            }
            p.sendMessage("§f§lCOMPASS §7::  §f나침반이 지급되었습니다.  §7(기존 아이템은 인벤의 다른 공간으로 옮겨졌습니다.)")
            p.inventory.itemInMainHand = COMPASS_ITEM
        } else {
            p.sendMessage("§f§lCOMPASS §7::  §f나침반이 지급되었습니다.")
            p.inventory.itemInMainHand = COMPASS_ITEM
        }
        return true
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onInteractWithCompass(e: PlayerInteractEvent) {
        val p = e.player
        if (!p.inventory.itemInMainHand.isSimilar(COMPASS_ITEM)
                || !ClassifyWorlds.isSpawnWorld(p.world)) {
            return
        }
        e.isCancelled = true
        WarpGUI(p)
    }


    // Class WarpGUI
    class WarpGUI(private val p: Player): Listener {
        private val gui = Bukkit.createInventory(null, 5*9, "스폰 워프 목록")
        private val itemWarpToTunnel = ItemStack(Material.CONCRETE).durabilityIB(8).nameIB("§6§l&m터널으로 이동").loreIB("§fˇ 추후 추가 예정인 터널입니다.", 2)
        private val itemWarpToHelicopter = ItemStack(Material.STAINED_CLAY).durabilityIB(14).nameIB("§c§l헬기장으로 이동").loreIB("§fˇ 헬기를 통해 §c전장§f에 나갈 수 있습니다.", 2)
        private val itemWarpToShop = ItemStack(Material.HARD_CLAY).nameIB("§6§l상점으로 이동").loreIB("§fˇ 상점에서 아이템을 §a구매§7/§c판매 §f할 수 있습니다.", 2)
        private val itemWarpToMemorialHall = ItemStack(Material.QUARTZ_BLOCK).nameIB("§6§l추모관으로 이동").loreIB("§fˇ 서버의 스토리가 담겨 있는 장소입니다.", 2)
        private val itemWarpToCenter = ItemStack(Material.STONE).durabilityIB(6).nameIB("§6§l중앙 광장으로 이동").loreIB("§fˇ 스폰의 중앙 광장입니다.", 2)

        init {
            Bukkit.getPluginManager().registerEvents(this, plugin)
            gui.run {
                this.setItem(10, itemWarpToTunnel)
                this.setItem(13, itemWarpToHelicopter)
                this.setItem(16, itemWarpToShop)
                this.setItem(28, itemWarpToMemorialHall)
                this.setItem(31, itemWarpToCenter)
            }
            p.openInventory(gui)
        }

        @EventHandler(priority = EventPriority.LOW)
        fun onClickGUI(e: InventoryClickEvent) {
            if (e.whoClicked != p || e.clickedInventory != gui || !ClassifyWorlds.isSpawnWorld(p.world)) return
            e.isCancelled = true
            val locationName: String
            val locationPos: Location
            when (e.currentItem) {
                itemWarpToTunnel -> {
                    p.sendMessage("§f§lCOMPASS §7:: §f터널 관련 컨텐츠는 §c추후 §f추가 예정입니다.")
                    return
                }
                itemWarpToHelicopter -> {
                    locationName = "헬기장"
                    locationPos = Location(p.world, 849.5, 88.0, 647.5, 0F, 0F)
                }
                itemWarpToShop -> {
                    locationName = "상점"
                    locationPos = Location(p.world, 897.5, 51.0, 769.5, -90F, 0F)
                }
                itemWarpToMemorialHall -> {
                    locationName = "추모관"
                    locationPos = Location(p.world, 766.5, 51.0, 814.5, 0F, 0F)
                }
                itemWarpToCenter -> {
                    locationName = "중앙 광장"
                    locationPos = Location(p.world, 842.5, 52.0, 784.5, -180F, 0F)
                }
                else -> {
                    return
                }
            }
            p.sendMessage("§f§lCOMPASS §7:: §6§l${locationName}§f으로 이동합니다.")
            p.teleport(locationPos)
            destroy()
        }

        @EventHandler
        fun onCloseInv(e: InventoryCloseEvent) {
            if (e.player != p) return
            destroy()
        }

        private fun destroy() {
            HandlerList.unregisterAll(this)
            if (p.openInventory.topInventory == gui) {
              p.closeInventory()
            }
        }
    }
}