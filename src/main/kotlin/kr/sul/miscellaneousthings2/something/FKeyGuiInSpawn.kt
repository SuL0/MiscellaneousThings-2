package kr.sul.miscellaneousthings2.something

import aaclans.main.ClanHandler
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.extensionfunction.WorldPlayers.getRealPlayers
import kr.sul.servercore.util.ClassifyWorlds
import kr.sul.servercore.util.ItemBuilder.durabilityIB
import kr.sul.servercore.util.ItemBuilder.loreIB
import kr.sul.servercore.util.ItemBuilder.nameIB
import kr.sul.servercore.util.ItemBuilder.unbreakableIB
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
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack

object FKeyGuiInSpawn: Listener {
    private val PREFIX = MsgPrefix.get("WARP")

    init {
        Bukkit.getScheduler().runTaskTimer(plugin, {
            for (spawnWorld in ClassifyWorlds.spawnWorlds) {
                for (p in spawnWorld.getRealPlayers()) {
                    p.sendActionBar("§8§l[ §f도움말 :: §8§l[§6§lF§8§l] §6§l메뉴 열기 §8§l]")
                }
            }
        }, 0L, 40L)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPressF(e: PlayerSwapHandItemsEvent) {
        if (e.isCancelled || !ClassifyWorlds.isSpawnWorld(e.player.world)) return
        e.isCancelled = true
        FKeyGUI(e.player)
    }




    // Class FKeyGUI
    class FKeyGUI(private val p: Player): Listener {
        val clanName = ClanHandler.getClanListener().getClan(p) ?: "X"
        private val gui = Bukkit.createInventory(null, 6*9, "")
        private val decorationItem = ItemStack(Material.FLINT_AND_STEEL, 1, 13)
            .nameIB("")
            .unbreakableIB(true)
        private val profileItem = ItemStack(Material.SKULL_ITEM, 1, 3)
            .nameIB("§8[ §6내 정보 §8] §f${p.name}")
            .loreIB("§7레벨: §f(업데이트 예정)")
            .loreIB("§7클래스: §6생존자 §c클래스 §b[기본]")
            .loreIB("§7소속 클랜: §f${clanName} §6[/클랜]")
            .loreIB("§7플레이 타임: §600일 00분 00초")
            .loreIB("§7잡은 생존자/좀비/감염자 수: §60 / 0 / 0 킬", 2)
            .loreIB("§7찾은 상자 개수: §60개")
        val helpItem = ItemStack(Material.PUMPKIN_SEEDS)
            .nameIB("§8[ §a자주묻는 질문 §8]")
            .loreIB("§f1 → &f자주 묻는 질문들을 모아둔 곳입니다.", 2)
            .loreIB("§f2 → &f관리자에게 질문하기 전 여기서 1차적으로 봐주시길 바랍니다.", 2)
        val playItem = ItemStack(Material.NETHER_BRICK_ITEM)
            .nameIB("§8[ §6생존시작 §8]")
            .loreIB("§7§o정부가 엄선한 §a안전한 이동포인트§7§o에서 생존을 시작합니다.", 2)
            .loreIB("      §7§o주의! 인벤토리 세이브가 되지 않습니다!")
            .loreIB("")
            .loreIB("§7도움말:")
            .loreIB(" §7command: &6/구조요청 §7or §6/spawn §7을 통해 이곳으로 되돌아올 수 있습니다.", 2)
        val shopItem = ItemStack(Material.STICK)
            .nameIB("§8[ §6상점 이동 §8]")
            .loreIB("")
            .loreIB("§f아이템을 사고 팔 수 있는 공간입니다.", 2)
        val tunnelItem = ItemStack(Material.SKULL_ITEM, 1, 5)
            .nameIB("§8[ §6개발 중 §8] §f터널 이동")
        val minelistItem = ItemStack(Material.EMERALD)
            .nameIB("§2[ §f마인리스트 §2] §f추천하기")
            .loreIB("§f서버를 추천하고 보상을 받아 가주세요 o_o/", 2)
        val vaultItem = ItemStack(Material.WHITE_SHULKER_BOX)
            .nameIB("§7[ §6개인창고 §7]")
            .loreIB("")
            .loreIB("§f얻은 아이템을 저장할 수 있습니다.", 2)
        val armoryItem = ItemStack(Material.DIAMOND_HOE)
            .nameIB("§8[ §6무기고 §8]")
            .loreIB("")
            .loreIB("§f구입한 스킨 무기를 착용/해제 할 수 있는 공간입니다.", 2)
        val dynmapItem = ItemStack(Material.MAP)
            .nameIB("§8[ §6위성지도 §8]")
            .loreIB("")
            .loreIB("§6생존지역§7에 누가 있는지 위성지도를 통해", 2)
            .loreIB("§7파악할 수 있습니다.")
        val discordItem = ItemStack(Material.RECORD_8)
            .nameIB("&8[ &9디스코드 주소 &8]")
            .loreIB("&f서버의 다양한 소식을 가장 빠르게 접해보세요.")
        val cafeItem = ItemStack(Material.RECORD_9)
            .nameIB("&8[ &a카페주소 &8]")
            .loreIB("&f덧글, 글쓰기를 통해 다양한 사람들과 소통할 수 있는 공간입니다.")
//        private val itemWarpToHelicopter = ItemStack(Material.STAINED_CLAY).durabilityIB(14).nameIB("§c§l헬기장으로 이동").loreIB("§fˇ 헬기를 통해 §c전장§f에 나갈 수 있습니다.", 2)

        init {
            Bukkit.getPluginManager().registerEvents(this, plugin)
            gui.run {
                this.setItem(4, profileItem)
                this.setItem(7, helpItem)
                this.setItem(22, playItem)
                this.setItem(28, shopItem)
                this.setItem(29, tunnelItem)
                this.setItem(30, minelistItem)
                this.setItem(32, vaultItem)
                this.setItem(33, armoryItem)
                this.setItem(34, dynmapItem)
                this.setItem(37, discordItem)
                this.setItem(38, cafeItem)
                this.setItem(53, decorationItem)
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
                vaultItem -> {
                    p.performCommand("/vault")
                }
                shopItem -> {
                    p.teleport(Location(p.world, 897.5, 51.0, 769.5, -90F, 0F))
                    p.sendMessage("${PREFIX}§6§l상점§f으로 이동합니다.")
                }

                else -> {
                    return
                }
            }
//            p.sendMessage("${PREFIX}§6§l${locationName}§f으로 이동합니다.")
//            p.teleport(locationPos)
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