package kr.sul.miscellaneousthings2.warpgui

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.miscellaneousthings2.warpgui.GuiItems.channel
import kr.sul.miscellaneousthings2.warpgui.GuiItems.world
import kr.sul.miscellaneousthings2.warpgui.data.WarpPlayerDataMgr
import kr.sul.servercore.nbtapi.NbtItem
import kr.sul.servercore.util.ClassifyWorlds
import kr.sul.servercore.util.ItemBuilder.nameIB
import kr.sul.servercore.util.ItemBuilder.unbreakableIB
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.util.Vector

object WarpGUI: Listener {
    private const val IS_GUI_OPENED_METAKEY = "WarpGUI: isGuiOpened"
    private const val GUI_SIZE = 9*8

    init {
        // 워프 블럭 근처로 다가가는지 확인
        Bukkit.getScheduler().runTaskTimer(plugin, {
            ClassifyWorlds.spawnWorlds.forEach { world ->
                world.players.filter { it.location.add(0.0, 1.0, 0.0).block.type == Material.PORTAL }.forEach { p ->
                    // 헬기 반대 방향으로 튕겨내기
                    var xVec = 1.5
                    var yVec = 0.6
                    if (p.location.clone().add(0.0, -0.1, 0.0).block.type != Material.PORTAL) {  // 점프하지 않은 상태라면 좀 더 위로 띄워줌
                        xVec *= 1.35
                        yVec *= 1.2
                    }
                    p.velocity = Vector(xVec, yVec, 0.0)
                    Bukkit.getScheduler().runTaskLater(plugin, {
                        if (!p.hasMetadata(IS_GUI_OPENED_METAKEY)) {
                            open(p)
                        }
                    }, 4L)
                }
            }
        }, 2L, 2L)
    }

    fun open(p: Player) {
        val pData = WarpPlayerDataMgr.getPlayerData(p)
        val testWorld = p.world  // TODO for test

        val inv = Bukkit.createInventory(p, GUI_SIZE, "")
        val ui = ItemStack(Material.FLINT_AND_STEEL, 1, 20).nameIB("§a").unbreakableIB(true)
        inv.setItem(49, ui)
        inv.setItem(3, GuiItems.normalButton.channel(1).world("Normal-1"))
        inv.setItem(4, GuiItems.normalButton.channel(1).world("Normal-1"))
        inv.setItem(5, GuiItems.normalButton.channel(1).world("Normal-1"))

        if (!pData.haveEverPlayedOnNormal) {
            // 아예 그냥 잠금
            inv.setItem(21, GuiItems.lockedButton)
            inv.setItem(22, GuiItems.lockedButton)
            inv.setItem(23, GuiItems.lockedButton)
        } else if (!pData.canPlayOnHard) {
            // 하드 테스트장
            inv.setItem(21, GuiItems.hardTestButton)
            inv.setItem(22, GuiItems.hardTestButton)
            inv.setItem(23, GuiItems.hardTestButton)
        } else {
            // 하드
            inv.setItem(21, GuiItems.hardButton.channel(1).world("Hard-1"))
            inv.setItem(22, GuiItems.hardButton.channel(1).world("Hard-1"))
            inv.setItem(23, GuiItems.hardButton.channel(1).world("Hard-1"))
        }

        inv.setItem(39, GuiItems.lockedButton)
        inv.setItem(40, GuiItems.lockedButton)
        inv.setItem(41, GuiItems.lockedButton)

        inv.setItem(57, GuiItems.lockedButton)
        inv.setItem(58, GuiItems.lockedButton)
        inv.setItem(59, GuiItems.lockedButton)

//        inv.setItem(39, GuiItems.hellButton.channel(1).currentPlayer(testWorld))
//        inv.setItem(40, GuiItems.hellButton.channel(2).currentPlayer(testWorld))
//        inv.setItem(41, GuiItems.hellButton.channel(3).currentPlayer(testWorld))
//
//        inv.setItem(57, GuiItems.silenceButton.channel(1).currentPlayer(testWorld))
//        inv.setItem(58, GuiItems.silenceButton.channel(2).currentPlayer(testWorld))
//        inv.setItem(59, GuiItems.silenceButton.channel(3).currentPlayer(testWorld))

        p.openInventory(inv)
        p.setMetadata(IS_GUI_OPENED_METAKEY, FixedMetadataValue(plugin, true))
    }

    @EventHandler
    fun onCloseWarpGUI(e: InventoryCloseEvent) {
        if (e.player.hasMetadata(IS_GUI_OPENED_METAKEY)) {
            e.player.removeMetadata(IS_GUI_OPENED_METAKEY, plugin)
        }
    }


    @EventHandler
    fun onWarpGuiClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        if (p.hasMetadata(IS_GUI_OPENED_METAKEY) && e.clickedInventory.size == GUI_SIZE) {
            e.isCancelled = true
            if (e.currentItem.type == Material.AIR) return
            val item = e.currentItem
            val nbti = NbtItem(item)
            if (nbti.tag.getString(GuiItems.WARP_NAME_KEY) != null) {
                val warpName = nbti.tag.getString(GuiItems.WARP_NAME_KEY)
                val channel = nbti.tag.getInt(GuiItems.CHANNEL_KEY)
                val worldName = nbti.tag.getString(GuiItems.WORLD_NAME_KEY)
                val world = Bukkit.getWorld(worldName)
                if (warpName != "잠김") {
                    warp(p, warpName, channel, world)
                }
            }
        }
    }
    private fun warp(p: Player, warpName: String, channel: Int, world: World) {
        if (warpName == "노말") {
            p.teleport(Location(world, 469.5, 70.0, 575.5))
            p.sendMessage("§c§lWARP: §f노멀-0$channel §7(으)로 텔레포트 되었습니다.")
        }
//        else if (warpName == "하드") {
//
//        }
    }
}