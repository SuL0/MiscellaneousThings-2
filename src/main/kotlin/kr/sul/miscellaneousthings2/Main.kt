package kr.sul.miscellaneousthings2

import kr.sul.miscellaneousthings2.chat.AreaChat
import kr.sul.miscellaneousthings2.chat.ChatInSpawn
import kr.sul.miscellaneousthings2.command.KillAllCommand
import kr.sul.miscellaneousthings2.command.NbtViewCommand
import kr.sul.miscellaneousthings2.endervaultsaddon.SelectorListener
import kr.sul.miscellaneousthings2.endervaultsaddon.VaultCommand
import kr.sul.miscellaneousthings2.something.HitAndDash
import kr.sul.miscellaneousthings2.knockdown.RideTest
import kr.sul.miscellaneousthings2.mob.spawner.EditMob
import kr.sul.miscellaneousthings2.mob.spawner.HardZombie
import kr.sul.miscellaneousthings2.mob.spawner.MobSpawner
import kr.sul.miscellaneousthings2.something.*
import kr.sul.miscellaneousthings2.something.block.*
import kr.sul.miscellaneousthings2.something.world.BackgroundMusicPlayer
import kr.sul.miscellaneousthings2.something.world.FixTimeInSomeWorlds
import kr.sul.miscellaneousthings2.something.world.spawn.SpawnWorldFeatures
import kr.sul.miscellaneousthings2.something.world.TpToSpawnWhenFirstJoin
import kr.sul.miscellaneousthings2.something.world.spawn.HidePlayersInSpawn
import kr.sul.miscellaneousthings2.warpgui.WarpGUI
import kr.sul.miscellaneousthings2.warpgui.data.WarpPlayerDataMgr
import kr.sul.servercore.something.BossBarTimer
import kr.sul.servercore.util.ObjectInitializer
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import xyz.upperlevel.spigot.book.BookUtil
import java.util.logging.Level

class Main : JavaPlugin(), Listener {
    companion object {
        internal lateinit var plugin: Plugin private set
        internal lateinit var instance: JavaPlugin private set
    }

    override fun onEnable() {
        plugin = this as Plugin
        instance = this
        registerClasses()
    }

    override fun onDisable() {
        WarpPlayerDataMgr.onPluginDisable()
    }

    private fun registerClasses() {
        Bukkit.getPluginManager().registerEvents(StopServerJoinTooEarly, plugin)
        Bukkit.getPluginManager().registerEvents(StopProjectileFromBreakingHanging, plugin)
        Bukkit.getPluginManager().registerEvents(TakeAwayPermissionIfNotOp, plugin)
        Bukkit.getPluginManager().registerEvents(SendResourcePack, plugin)
        Bukkit.getPluginManager().registerEvents(KillAllCommand, plugin)
        Bukkit.getPluginManager().registerEvents(EditMob, plugin)
        Bukkit.getPluginManager().registerEvents(PreventArmorstandFromBreaking, plugin)
        Bukkit.getPluginManager().registerEvents(InvalidateSomeHealAndDamage, plugin)
        Bukkit.getPluginManager().registerEvents(ChatInSpawn, plugin)
        Bukkit.getPluginManager().registerEvents(RideTest, plugin)
        Bukkit.getPluginManager().registerEvents(DamageScouter, plugin)
        Bukkit.getPluginManager().registerEvents(AreaChat, plugin)
        Bukkit.getPluginManager().registerEvents(WarpGUI, plugin)
        Bukkit.getPluginManager().registerEvents(WarpPlayerDataMgr, plugin)
        Bukkit.getPluginManager().registerEvents(CombineArmor, plugin)
        Bukkit.getPluginManager().registerEvents(ForceSneakToPickUp, plugin)
        Bukkit.getPluginManager().registerEvents(PreventToPickUpVanillaItem, plugin)
        Bukkit.getPluginManager().registerEvents(VaultCommand, plugin)
        Bukkit.getPluginManager().registerEvents(TwoAutoToRektKiro, plugin)
        Bukkit.getPluginManager().registerEvents(BackgroundMusicPlayer, plugin)
        Bukkit.getPluginManager().registerEvents(TpToSpawnWhenFirstJoin, plugin)
        Bukkit.getPluginManager().registerEvents(RaiderWithBook, plugin)
        Bukkit.getPluginManager().registerEvents(HitAndDash, plugin)
        Bukkit.getPluginManager().registerEvents(BlockLeftHand, plugin)
        Bukkit.getPluginManager().registerEvents(SpawnWorldFeatures, plugin)
        Bukkit.getPluginManager().registerEvents(HidePlayersInSpawn, plugin)
        Bukkit.getPluginManager().registerEvents(HardZombie, plugin)
        ObjectInitializer.forceInit(MobSpawner::class.java)
        ObjectInitializer.forceInit(SelectorListener::class.java)
        ObjectInitializer.forceInit(FixTimeInSomeWorlds::class.java)

        getCommand("nbtview").executor = NbtViewCommand

        // TODO FOr TEST!
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    // TODO TEST
    @EventHandler
    fun testCommand(e: PlayerCommandPreprocessEvent) {
        if (!e.player.isOp) return
//
//        if (e.message == "/book") {
//            val page1 = BookUtil.PageBuilder().add("     < 가이드 북 >\n")
//                .add(BookUtil.TextBuilder.of("[여기]").onHover(BookUtil.HoverAction.showText(
//                    "[ 노말에서 살아남기 ]\n" +
//                            "기록의 문을 넘어 노말 난이도에 입장하세요.\n" +
//                            "입장하신 뒤에 노말 난이도가 쉬워질 때 까지\n" +
//                            "적응 하셔야 합니다.\n")).build()
//                )
//                .add(BookUtil.TextBuilder.of(" 노말에서 살아남기\n").build())
//                .add(BookUtil.TextBuilder.of(" [스폰으로 가기]").onClick(BookUtil.ClickAction.runCommand("/spawn")).build())
//                .build() // 페이지 빌드
//
//
//            val bookItem = BookUtil.writtenBook()
//                .author("저자").title("제목")
//                .pages(page1)
//                .build()
//
//            e.player.inventory.addItem(bookItem)
//        }
//
//




        if (e.message.startsWith("/dur") && e.message.contains(" ")) {
            e.isCancelled = true
            val arg1 = e.message.split(" ")[1].toShort()
            val item = e.player.inventory.itemInMainHand
            item.durability = (item.durability - arg1).toShort()
        }

        if (e.message == "/아이템추출") {
            e.isCancelled = true
            val item = e.player.inventory.itemInMainHand
            Bukkit.getLogger().log(Level.INFO, "-----------------------")
            Bukkit.getLogger().log(Level.INFO, item.itemMeta.displayName.replace("§", "&"))
            item.itemMeta.lore.forEach {
                Bukkit.getLogger().log(Level.INFO, it.replace("§", "&"))
            }
        }

        if (e.message == "/타이머") {
            e.isCancelled = true
            val bossBarTitleLambda: (Int) -> (String) = { leftTime ->
                val leftTimeStr = run {
                    val min = parseIntToTwoDigitStr(leftTime/60)
                    val sec = parseIntToTwoDigitStr(leftTime%60)
                    "§c§l${min}§7분 §c§l${sec}§7초"
                }
                "§4§lBOSS: §f남은시간 $leftTimeStr"
            }
            BossBarTimer.setTimer(e.player, 10, bossBarTitleLambda, BarColor.PURPLE, BarStyle.SOLID, BarFlag.DARKEN_SKY)
        }

        if (e.message.startsWith("/gui")) {
            val line: Int
            try {
                line = e.message.split(" ")[1].toInt()
            } catch (ignored: Exception) {
                e.player.sendMessage("§c§lCMD: §7/gui <1~99>")
                return
            }
            val inv = Bukkit.createInventory(null, line*9)
            e.player.openInventory(inv)
        }

        if (e.message == "/book") {
            e.isCancelled = true
            val page1 = BookUtil.PageBuilder().add("      §6§l§o§n< 가이드 북 >\n")
                .add(
                    BookUtil.TextBuilder.of("\n\n§c§l[여기]").onHover(
                        BookUtil.HoverAction.showText(
                            "§c[ §f§l노말에서 살아남기 §c] §f§l- §e§l§o(진행중)\n" +
                                    "\n§7기록의 문을 넘어 노말 난이도에 입장하세요.\n" +
                                    "§7입장하신 뒤에 노말 난이도가 쉬워질 때 까지\n" +
                                    "§7적응 하셔야 합니다.\n\n" +
                                    "§f§l가디언 §c처치하기 §e20회 §f§l<달성여부: §4✕§f§l>\n" +
                                    "  §f§l좀비 §c처치하기 §e20회 §f§l<달성여부: §4✕§f§l>\n" +
                                    "  §f§lScav §c처치하기  §e3회 §f§l<달성여부: §4✕§f§l>\n\n" +
                                    "        §e§l>> §f§l보상: §e【 §f선별시험자격§e 】"
                        )
                    ).build()
                )
                .add(BookUtil.TextBuilder.of(" §0▶ 노말에서 살아남기\n").build())
                .add(
                    BookUtil.TextBuilder.of("\n\n§c§l[여기]").onHover(
                        BookUtil.HoverAction.showText(
                            "§c[ §f§l최종선별 §c] §f§l- §e§l§o(진행중)\n" +
                                    "\n§7선별시험자격을 얻으셨다면 \n더 이상 노말은 무의미합니다\n" +
                                    "§7기록의 문을 넘어 시험장에 입장하세요.\n" +
                                    "§7그곳에 있는 스케브 잔당들을 섬멸하세요\n\n" +
                                    "§e§l하드선별시험 §f§l통과 §f§l<달성여부: §4✕§f§l>\n\n" +
                                    "        §e§l>> §f§l보상: §c【 §f하드입장권한§c 】"
                        )
                    ).build()
                )
                .add(BookUtil.TextBuilder.of(" §0▶ 최종선별\n").build())
                .add(
                    BookUtil.TextBuilder.of("\n\n§c§l[여기]").onHover(
                        BookUtil.HoverAction.showText(
                            "§c[ §f§l하드에서 살아남기 §c] §f§l- §e§l§o(진행중)\n" +
                                    "\n§7더 이상의 안내 지침은 필요없습니다!\n" +
                                    "§7필요한 과정을 모두 거치셨으며 \n하드로 떠나셔도 좋습니다\n" +
                                    "§7자유롭게 파티를 맺어 \nFPS 약탈을 즐겨주시길 바랍니다\n\n§a감사합니다 ◠‿◠ - 서버일동 -\n\n" +
                                    "§c【 §f하드입장권한§c 】 §f§l획득 §f§l<달성여부: §4✕§f§l>\n\n" +
                                    "        §e§l>> §f§l보상: §2【 §f빛나는 보물상자§2 】"
                        )
                    ).build()
                )
                .add(BookUtil.TextBuilder.of(" §0▶ 하드에서 살아남기\n").build())

                .build() // 페이지 빌드


            val bookItem = BookUtil.writtenBook()
                .author("Server").title("§a§l§o가이드북")
                .pages(page1)
                .build()
        }
    }
    private fun parseIntToTwoDigitStr(i: Int): String {
        if (i < 10) {
            return "0$i"
        }
        return "$i"
    }
}