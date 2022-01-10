package kr.sul.miscellaneousthings2.something

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.extensionfunction.WorldPlayers.getRealPlayers
import kr.sul.servercore.util.ClassifyWorlds
import kr.sul.servercore.util.ItemBuilder.durabilityIB
import kr.sul.servercore.util.ItemBuilder.loreIB
import kr.sul.servercore.util.ItemBuilder.nameIB
import kr.sul.servercore.util.MsgPrefix
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack

object WarpHelperInSpawn: Listener {
    private val PREFIX_COMPASS = MsgPrefix.get("COMPASS")

    init {
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (spawnWorld in ClassifyWorlds.spawnWorlds) {
                for (p in spawnWorld.getRealPlayers()) {
                    p.sendActionBar("§8§l[ §f조작안내 :: §8§l[F] §6§l메뉴 열기 §8§l]")
                }
            }
        }, 0L, 40L)
    }
    @EventHandler
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        if (e.message.startsWith("/c ")) {
            var msg = e.message.removeRange(0, 2)
            msg = msg.replace("&", "§")
            Bukkit.getOnlinePlayers().forEach {
                it.sendMessage(msg)
            }
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPressF(e: PlayerSwapHandItemsEvent) {
        if (e.isCancelled || !ClassifyWorlds.isSpawnWorld(e.player.world)) return
        e.isCancelled = true
        WarpGUI(e.player)
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
                    p.sendMessage("${PREFIX_COMPASS}§f터널 관련 컨텐츠는 §c추후 §f추가 예정입니다.")
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
            p.sendMessage("${PREFIX_COMPASS}§6§l${locationName}§f으로 이동합니다.")
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