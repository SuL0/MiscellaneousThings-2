package kr.sul.miscellaneousthings2

/* import kr.sul.miscellaneousthings2.something.HitAndDash */
//import kr.sul.miscellaneousthings2.something.world.spawn.SpawnWorldFeatures
import com.github.shynixn.mccoroutine.launch
import kotlinx.coroutines.delay
import kr.sul.miscellaneousthings2.chat.AreaChat
import kr.sul.miscellaneousthings2.chat.ChatInSpawn
import kr.sul.miscellaneousthings2.combatlog.CombatLog
import kr.sul.miscellaneousthings2.command.EconomyCommand
import kr.sul.miscellaneousthings2.command.KillAllCommand
import kr.sul.miscellaneousthings2.command.NbtViewCommand
import kr.sul.miscellaneousthings2.customitem.CustomItemMain
import kr.sul.miscellaneousthings2.customitem.armor.ArmorMgr
import kr.sul.miscellaneousthings2.customitem.armor.ArmorVendingMachine
import kr.sul.miscellaneousthings2.customitem.food.FoodDefined
import kr.sul.miscellaneousthings2.customitem.melee.MeleeWeaponDefined
import kr.sul.miscellaneousthings2.endervaultsaddon.SelectorListener
import kr.sul.miscellaneousthings2.endervaultsaddon.VaultCommand
import kr.sul.miscellaneousthings2.knockdown.RideTest
import kr.sul.miscellaneousthings2.mob.spawner.MobSpawner
import kr.sul.miscellaneousthings2.mob.spawner.editmob.EditMob
import kr.sul.miscellaneousthings2.mob.spawner.editmob.TestZombie
import kr.sul.miscellaneousthings2.something.*
import kr.sul.miscellaneousthings2.something.block.*
import kr.sul.miscellaneousthings2.something.world.BackgroundMusicPlayer
import kr.sul.miscellaneousthings2.something.world.TpToSpawnWhenFirstJoin
import kr.sul.miscellaneousthings2.something.world.spawn.SpawnWorldFeatures
import kr.sul.miscellaneousthings2.tutorial.TutorialPlayer
import kr.sul.miscellaneousthings2.tutorial.captcha.CaptchaPlayer
import kr.sul.miscellaneousthings2.warpgui.WarpGUI
import kr.sul.miscellaneousthings2.warpgui.data.WarpPlayerDataMgr
import kr.sul.miscellaneousthings2.warptobeachtown.FountainOfLife
import kr.sul.miscellaneousthings2.warptobeachtown.WarpToBeachtownWorld
import kr.sul.servercore.something.BossBarTimer
import kr.sul.servercore.util.ItemBuilder.nameIB
import kr.sul.servercore.util.MsgPrefix
import kr.sul.servercore.util.ObjectInitializer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import xyz.upperlevel.spigot.book.BookUtil
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*
import java.util.logging.Level
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.math.round
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class Main : JavaPlugin(), Listener {
    companion object {
        internal lateinit var plugin: Plugin private set
        internal lateinit var instance: JavaPlugin private set
    }

    override fun onEnable() {
        plugin = this
        instance = this
        registerClasses()
    }

    override fun onDisable() {
        WarpPlayerDataMgr.onPluginDisable()
        WarpToBeachtownWorld.onPluginDisable()
    }

    private fun registerClasses() {
        Bukkit.getPluginManager().registerEvents(StopServerJoinTooEarly, plugin)
        Bukkit.getPluginManager().registerEvents(TakeAwayPermissionIfNotOp, plugin)
        Bukkit.getPluginManager().registerEvents(SendResourcePack, plugin)
        Bukkit.getPluginManager().registerEvents(KillAllCommand, plugin)
        Bukkit.getPluginManager().registerEvents(EditMob, plugin)
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
        Bukkit.getPluginManager().registerEvents(TwoAuthToRektKiro, plugin)
        Bukkit.getPluginManager().registerEvents(BackgroundMusicPlayer, plugin)
        Bukkit.getPluginManager().registerEvents(TpToSpawnWhenFirstJoin, plugin)
//        Bukkit.getPluginManager().registerEvents(RaiderWithBook, plugin)
//        Bukkit.getPluginManager().registerEvents(HitAndDash, plugin)
        Bukkit.getPluginManager().registerEvents(BlockLeftHand, plugin)
//        Bukkit.getPluginManager().registerEvents(SpawnWorldFeatures, plugin)
//        Bukkit.getPluginManager().registerEvents(HidePlayersInSpawn, plugin)
//        Bukkit.getPluginManager().registerEvents(HardZombie, plugin)
//        Bukkit.getPluginManager().registerEvents(GlowAllPlayersInNormal, plugin)
//        Bukkit.getPluginManager().registerEvents(MeleeAttackMechanism, plugin)
        Bukkit.getPluginManager().registerEvents(TestZombie, plugin)
        Bukkit.getPluginManager().registerEvents(FKeyGuiInSpawn, plugin)
        Bukkit.getPluginManager().registerEvents(ArmorWeight, plugin)
        Bukkit.getPluginManager().registerEvents(BlueberryInBush, plugin)
        Bukkit.getPluginManager().registerEvents(WorldProtect, plugin)
        Bukkit.getPluginManager().registerEvents(PlayerJoinMessage, plugin)
        Bukkit.getPluginManager().registerEvents(DebugCommand, plugin)
        Bukkit.getPluginManager().registerEvents(CombatLog, plugin)
        Bukkit.getPluginManager().registerEvents(ArmorMgr, plugin)
        Bukkit.getPluginManager().registerEvents(SpawnWorldFeatures, plugin)
        ObjectInitializer.forceInit(MobSpawner::class.java)
        ObjectInitializer.forceInit(SelectorListener::class.java)
//        ObjectInitializer.forceInit(FixTimeInSomeWorlds::class.java)
        ObjectInitializer.forceInit(AutoReload::class.java)
        ObjectInitializer.forceInit(FountainOfLife::class.java)
        ObjectInitializer.forceInit(CustomItemMain::class.java)

        PacketViewer.register()


        getCommand("nbtview").executor = NbtViewCommand
        getCommand("돈").executor = EconomyCommand
        getCommand("인사하기").executor = PlayerJoinMessage
        getCommand("이동지점알림끄기").executor = WarpToBeachtownWorld
        getCommand("방어구수정").executor = ArmorVendingMachine

        // TODO FOr TEST!
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    // TODO TEST
    @OptIn(ExperimentalTime::class)
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




        if (e.message.startsWith("/dur")) {
            e.isCancelled = true
            if (e.message.contains(" ")) {
                val arg1 = e.message.split(" ")[1].toShort()
                val item = e.player.inventory.itemInMainHand
                item.durability = (item.durability - arg1).toShort()
            } else if (e.message == "/dur") {
                e.player.sendMessage("dur: ${e.player.inventory.itemInMainHand.durability}")
            }
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

        if (e.message == "/book1") {
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
        if (e.message == "/tutorial") {
            e.isCancelled = true
            TutorialPlayer(e.player)
        }
        if (e.message == "/testmob") {
            e.isCancelled = true
            NpcTest.run(e.player)
        }
        if (e.player.isOp && e.message.startsWith("/c ")) {
            e.isCancelled = true
            var msg = e.message.removeRange(0, 2)
            msg = msg.replace("&", "§")
            Bukkit.getOnlinePlayers().forEach {
                it.sendMessage(msg)
            }
        }
//        if (e.message == "/asynct") {
//            e.isCancelled = true
//            Bukkit.broadcastMessage("thread : ${Thread.currentThread().name}")
//            val elapsed = measureTimeMillis {
//                for (i in 1..10000) {
//                    AppropriateLocFinder.find(e.player, arrayListOf(Material.CONCRETE))
//                }
//            }
//            Bukkit.broadcastMessage("sync-took ${elapsed}ms")
//            launchAsync {
//                Bukkit.broadcastMessage("thread : ${Thread.currentThread().name}")
//                val elapsed = measureTimeMillis {
//                    for (i in 1..10000) {
//                        AppropriateLocFinder.find(e.player, arrayListOf(Material.CONCRETE))
//                    }
//                }
//                Bukkit.broadcastMessage("async-took ${elapsed}ms")
//            }
//        }
        if (e.message == "/book2") {
            e.isCancelled = true
            val page1 = BookUtil.PageBuilder()
                .add("\n")
                .add(
                    BookUtil.TextBuilder.of("§0§nQ. 이곳은 뭐하는 서버인가요?\n")
                        .onHover(BookUtil.HoverAction.showText(
                            "A. §a생존§f이라는 큰 틀 속에서 정답은 여러가지 있습니다.\n" +
                            "§c뭐하는 서버§f인지는 §c생존자 §f분들께서 서버를 플레이 하시면서 답을 찾으실겁니다.    \n" +
                            "§f지금은 그저 당장 뭘 해야되는지 알고 계시는게 좋을것 같습니다"
                        )).build()
                )
                .add(
                    BookUtil.TextBuilder.of("\n§0§nQ. 처음엔 뭘 해야하나요?\n")
                        .onHover(BookUtil.HoverAction.showText(
                            "A. §8[§6F§8] §f메뉴키 를 통해 §a생존§f을 시작하시면 됩니다.\n" +
                                    "§f맵 곳곳에는 §c생존자분들을 §f위한 §b구호물자 보급품§f이 다수 존재합니다.\n" +
                                    "§f집안 곳곳에 존재하니 찾아 얻으신 뒤 이 §a세계에서 적응하고 살아간다는걸 §f배워가셨으면 좋겠습니다.    "
                        )).build()
                )
                .add(
                    BookUtil.TextBuilder.of("\n§0§nQ. 특정 명령어에 대해 알고싶어요\n")
                        .onHover(BookUtil.HoverAction.showText(
                            "A. §7§ocommand: §6/명령어 §f을 입력하면 §c생존자§f 분들이\n" +
                                    "서버를 플레이하시면서 궁금해하는 명령어 들을 보기좋게 카테고리별로 정리해놨습니다.    "
                        )).build()
                )
                .add(
                    BookUtil.TextBuilder.of("\n§0§nQ. 이 서버에 후원을 하고싶어요 보상은 있나요?\n")
                        .onHover(BookUtil.HoverAction.showText(
                            "A. 죄송하게도 무언가를 바라고 후원하신다면 받지 않습니다.\n" +
                                    "다만 꾸미는걸 좋아하시는 유저분들에 한에 §c[기동] §2[수색] §e[특공] 중 1개를 칭호로 드리겠습니다.    "
                        )).build()
                )

                .add(
                    BookUtil.TextBuilder.of("\n§0§nQ. 관리자분께 문의 드릴 내용이 있어요\n")
                        .onHover(BookUtil.HoverAction.showText(
                            "혹은 서버에 관리자가 존재할 경우 §7§ocommand: §e/관리자 호출 §f을 통해 불러주시길 바랍니다.    \n" +
                                    "메시지/호출 간에는 반드시 문의를해야 하는 내용인지 다시 검토해주시길 바랍니다.\n" +
                                    "관리자는 시스템이 아닌 사람이기에 개인시간이 필요한 존재입니다."
                        )).build()
                )
                .build() // 페이지 빌드


            val bookItem = BookUtil.writtenBook()
                .author("저자").title("제목")
                .pages(page1)
                .build()

            e.player.inventory.addItem(bookItem)
        }
        if (e.message == "/nms") {
            e.isCancelled = true
            val item = e.player.inventory.itemInMainHand
            val nmsItem = (item as CraftItemStack).handle
            nmsItem.tag!!.setBoolean("Unbreakable", !nmsItem.tag!!.getBoolean("Unbreakable"))
        }

        if (e.message == "/bytebuddy") {
            // 정작 코드는 JavaProxy
            e.isCancelled = true

//            val targetString = "테스트"
            val meta = e.player.inventory.itemInMainHand.itemMeta
            val invocationHandler = object: InvocationHandler {
                val target = meta  // meta와는 다른 객체로 설정됨 < ??
                override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any {
                    if (args == null) {
                        return method.invoke(target) // parameter에 뭐가 있어도 args는 null인데?
                    }
                    return method.invoke(target, *args)
                }
            }
            val proxiedMeta = Proxy.newProxyInstance(
                ItemMeta::class.java.classLoader,
//                invocationHandler::class.java.classLoader,
                arrayOf(ItemMeta::class.java),
                invocationHandler
            ) as ItemMeta
            Bukkit.broadcastMessage(proxiedMeta.displayName)

            Bukkit.broadcastMessage("proxiedMeta ${proxiedMeta.lore.size}")
            proxiedMeta.lore.add("Is this really not added in meta?")
            Bukkit.broadcastMessage("proxiedMeta ${proxiedMeta.lore.size}")
            Bukkit.broadcastMessage("meta ${meta.lore.size}")
            meta.lore.add("Is this really not added in meta?")
            Bukkit.broadcastMessage("meta ${meta.lore.size}")


//            try {
//                Bukkit.broadcastMessage(proxiedMeta.lore.get(0))
//            } catch (ignored: Exception) {}
//            try {
//                Bukkit.broadcastMessage(proxiedMeta.displayName)
//            } catch (ignored: Exception) {}

//
//            val dynamicType = ByteBuddy()
//                .subclass(ItemMeta::class.java)
//                .method(ElementMatchers.any())
//                .intercept(InvocationHandlerAdapter.of(invocationHandler))
//                .make()
//                .load(ItemMeta::class.java.classLoader)
//                .loaded
//            Bukkit.broadcastMessage("${dynamicType.newInstance()}")

        }
        if (e.message == "/asBukkitMirror") {
            e.isCancelled = true
            val item = e.player.inventory.itemInMainHand
            val nmsItem = (item as CraftItemStack).handle
            val mirror = nmsItem.asBukkitMirror()
            mirror.amount += 1
            Bukkit.broadcastMessage("${item.amount}  vs  ${mirror.amount}")
        }
        if (e.message == "/craftItemStack") {
            e.isCancelled = true
            val chestPlate = ItemStack(Material.DIAMOND_CHESTPLATE).nameIB("커맨드로 생성된 갑옷")
            e.player.inventory.chestplate = chestPlate

            Bukkit.broadcastMessage("§ae.player.inventory.chestplate is CraftItemStack?: ${e.player.inventory.chestplate is CraftItemStack}")
            Bukkit.getScheduler().runTask(plugin) {
                Bukkit.broadcastMessage("§a[1Tick]e.player.inventory.chestplate is CraftItemStack?: ${e.player.inventory.chestplate is CraftItemStack}")
            }
        }
        if (e.message == "/captcha") {
            e.isCancelled = true
            val captchaDigitSet = CaptchaPlayer.CaptchaDigitSet(0, 1)
            captchaDigitSet.display(e.player)
        }
        if (e.message == "/checkImInHouse") {
            e.isCancelled = true
            val world = e.player.world
            val loc = e.player.location
            Bukkit.broadcastMessage("${world.getBlockAt(loc.blockX, loc.blockY, loc.blockZ).lightFromSky}")
            return
            val loopTimes = 10000000
            val elapsedTime = measureTimeMillis {
                for (i in 1..loopTimes) {
                    world.getBlockAt(loc.blockX, loc.blockY+1, loc.blockZ).lightFromSky
                }
            }
            val elapsedTime2 = measureTimeMillis {
                for (i in 1..loopTimes) {
                    for (i in 1..5) {
                        if (world.getBlockAt(loc.blockX, loc.blockY+i, loc.blockZ).type == Material.AIR) {

                        }
                    }
                }
            }
            Bukkit.broadcastMessage("lightFromSky: ${elapsedTime}")
            Bukkit.broadcastMessage("just check y: ${elapsedTime2}")
        }
        if (e.message == "/checkRunningSpeed1") {
            e.isCancelled = true
            val origLoc = e.player.location
            plugin.launch {
                for (i in 1..40) {
                    delay(1)
                    if (origLoc.distance(e.player.location) >= 0.001) {
                        Bukkit.broadcastMessage("start measuring")
                        val loc1 = e.player.location
                        delay(Duration.Companion.seconds(10))
                        loc1.y = e.player.location.y
                        Bukkit.broadcastMessage("${e.player.name}'s speed : ${round(e.player.location.distance(loc1) * 100) / (100.0 * 10)}")
                        return@launch
                    }
                }
            }
        }
        if (e.message == "/checkRunningSpeed2") {
            plugin.launch {
                for (i in 1..100) {
                    val pastLoc = e.player.location
                    delay(1)
                    pastLoc.y = e.player.location.y
                    Bukkit.broadcastMessage("${e.player.name}'s speed 1 : ${round(e.player.location.distance(pastLoc)*1000)/1000.0}")
                }
            }
        }
        if (e.message == "/customItemStack") {
            if (e.player.inventory.itemInMainHand.type != Material.AIR) {
                (e.player.inventory.itemInMainHand as TestItemStack).imCustom()
                return
            }
            val item = CraftItemStack.asCraftMirror(CraftItemStack.asNMSCopy(TestItemStack(Material.DIAMOND)))
            e.player.inventory.itemInMainHand = item
            Bukkit.broadcastMessage("${item === e.player.inventory.itemInMainHand}")
            Bukkit.broadcastMessage("${item.handle == (e.player.inventory.itemInMainHand as CraftItemStack).handle}")
            (e.player.inventory.itemInMainHand as TestItemStack).imCustom()
        }





        if (e.message == "/nbtSpeed") {
            e.isCancelled = true
            if (e.player.inventory.itemInMainHand.type != Material.AIR) {
                e.player.sendMessage("${MsgPrefix.get("TEST")} 손의 아이템을 비워야 합니다.")
                return
            }
            val item = ItemStack(Material.DIAMOND)
            e.player.inventory.itemInMainHand = item
            val itemInHand = (e.player.inventory.itemInMainHand as CraftItemStack)
            itemInHand.handle.tagOrDefault.setString("testStr", UUID.randomUUID().toString())
            itemInHand.handle.tagOrDefault.setInt("testInt", nextInt( 100000))
            itemInHand.handle.tagOrDefault.setBoolean("testBool", true)

//            val howManyTimes = 500000000
            val howManyTimes = 10000000

            plugin.launch {

                delay(1)

                val timer = measureTimeMillis {
                    for (i in 1..howManyTimes) {
                        itemInHand.handle.tagOrDefault.setString("testStr", nextInt(10).toString())
                    }
                }
                Bukkit.broadcastMessage("${howManyTimes}-str : ${timer/1000.0}s  ${timer}ms")
                delay(1)

                val timer2 = measureTimeMillis {
                    for (i in 1..howManyTimes) {
                        itemInHand.handle.tagOrDefault.setInt("testInt", nextInt(10))
                    }
                }
                Bukkit.broadcastMessage("${howManyTimes}-int : ${timer2/1000.0}s  ${timer2}ms")
                delay(1)

                val timer3 = measureTimeMillis {
                    for (i in 1..howManyTimes) {
                        itemInHand.handle.tagOrDefault.setBoolean("testBool", nextBoolean())
                    }
                }
                Bukkit.broadcastMessage("${howManyTimes}-bool : ${timer3/1000.0}s  ${timer3}ms")
                delay(1)

                var a = "aaaaa"
                val timer4 = measureTimeMillis {
                    for (i in 1..howManyTimes) {
                        a = nextInt(10).toString()
                    }
                }
                Bukkit.broadcastMessage("${howManyTimes}-val : ${timer4/1000.0}s  ${timer4}ms")

                val timer5 = measureTimeMillis {
                    for (i in 1..howManyTimes) {
                        val nms = CraftItemStack.asNMSCopy(itemInHand)
                        nms.tagOrDefault.setInt("testInt", nextInt(10))
                    }
                }
                Bukkit.broadcastMessage("${howManyTimes}-past way : ${timer5/1000.0}s  ${timer5}ms")

                val timer6 = measureTimeMillis {
                    for (i in 1..howManyTimes) {
                        val newObj = TestItemWrapper(item)
                    }
                }
                Bukkit.broadcastMessage("${howManyTimes}-obj creation : ${timer6/1000.0}s  ${timer6}ms")


                greaterThan<Int>(1, 3)
            }
        }
    }

    private fun parseIntToTwoDigitStr(i: Int): String {
        if (i < 10) {
            return "0$i"
        }
        return "$i"
    }

    // T: Comparable<Any> 라면 이해하는데 어떻게 T: Comparable<T> 가 될 수 있지? 순환참조 아닌가?
    // -> Int : Comparable<Int>
    // T == Comparable<T> 로 해석하면 안됨
    // 일단 T에 타입을 넣고 T: Comparable<T> 로 해석하면 됨. 그냥 클래스처럼 Int : Comparable<Int>
    private fun <T : Comparable<T>> greaterThan(lhs: T, rhs: T): Boolean {
        return lhs > rhs
    }
    private fun gen(a: Array<out Int>) {

    }


//    lateinit var dashMap : EntityTempDataMap<Int>
//    fun init() {
//        dashMap = EntityTempDataMap.create(plugin)
//        Bukkit.getScheduler().runTaskTimer(plugin, {
//        Bukkit.getOnlinePlayers().forEach { p ->
//        if (p.isSprinting) {
//            if (dashMap.containsKey(p)) {
//                dashMap[p] = dashMap[p]!!+1
//            } else {
//                dashMap[p] = 1
//            }
//            p.walkSpeed = 0.2F + dashMap[p]!!/400F
//            Bukkit.broadcastMessage("${p.walkSpeed}")
//        }
//        else {
//            if (dashMap.containsKey(p)) {
//                dashMap.remove(p)
//                p.walkSpeed = 0.2F
//                Bukkit.broadcastMessage("§c${p.walkSpeed} reset")
//            }
//        }
//        }
//        }, 1L, 2L)
//    }
}

class TestItemStack(type: Material): ItemStack(type) {
    fun imCustom() {
        Bukkit.broadcastMessage("yeah")
    }
}
class TestItemWrapper(val item: ItemStack) {
    fun sdf() {
        Bukkit.broadcastMessage(item.type.toString())
    }
}