package kr.sul.miscellaneousthings2.warpgui

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.file.simplylog.LogLevel
import kr.sul.servercore.file.simplylog.SimplyLog
import kr.sul.servercore.util.ItemBuilder.loreIB
import kr.sul.servercore.util.ItemBuilder.nameIB
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

object GuiItems {
    const val WARP_NAME_KEY = "WarpGui.GuiItems: WarpName"
    const val CHANNEL_KEY = "WarpGui.GuiItems: Channel"
    const val WORLD_NAME_KEY = "WarpGui.GuiItems: WorldName"

    val normalButton: ItemStack
        get() = CraftItemStack.asCraftMirror(CraftItemStack.asNMSCopy(ItemStack(Material.SKULL_ITEM, 1, 3))).warpName("노말")
            .nameIB("§a§l:: 노말 %channel%채널 :: §c[ §f%current_player% / 20 §c]")
            .loreIB("§f서버에 처음 입문하시는 분이라면")
            .loreIB("§f추천드리는 난이도입니다.")
            .loreIB("&7&m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m")
            .loreIB("&7&l정보: &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &7&l도움말:")
            .loreIB("&f 난이도: &7■□□ &f &f &f &f &f &f &f &f &f &f &f &f &f없음")
            .loreIB("&f 형태: &7유적지")
            .loreIB("&f 날씨: &7맑음")
            .loreIB("&f")
            .loreIB("&7&l배경:")
            .loreIB("&7언데드 사태 이후 수십 세기가 지난 현재", 2)
            .loreIB("&7권능의 힘은 쇠퇴해갔고,")
            .loreIB("&7권속인 망자들의 힘도 약해져 갔다.")
            .loreIB("&7수십 세기가 지난 지금 생존자들은")
            .loreIB("&7남아있는 물자를 찾기 위해")
            .loreIB("&7지금은 유적지가 된 이곳을 찾는다...")
    val hardButton: ItemStack
        get() = CraftItemStack.asCraftMirror(CraftItemStack.asNMSCopy(ItemStack(Material.SKULL_ITEM, 1, 2))).warpName("하드")
            .nameIB("&c&l:: 하드 %channel%채널 :: &c[ &f%current_player% / 50 &c]")
            .loreIB("&f플레이에 숙련되신 분들께 제공하는")
            .loreIB("&f난이도입니다.")
            .loreIB("&7&m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m")
            .loreIB("&7&l정보: &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &7&l도움말:")
            .loreIB("&f 난이도: &7■■□ &f &f &f &f &f &f &f &f &f &f &f &f &f[/파티] 권장")
            .loreIB("&f 형태: &7폐허 도시")
            .loreIB("&f 날씨: &7맑음")
            .loreIB("&f")
            .loreIB("&7&l배경:")
            .loreIB("&7언데드 사태 이후 수 세기가 지난날")
            .loreIB("&7권능의 힘이 점차 잃어가며")
            .loreIB("&7권속들의 힘이 다소 약해졌다.")
            .loreIB("&7이날의 도시는 아직까지 형태를 유지하고 있다.", 2)
    val hardTestButton: ItemStack
        get() = CraftItemStack.asCraftMirror(CraftItemStack.asNMSCopy(ItemStack(Material.RECORD_5, 1))).warpName("하드 테스트")
            .nameIB("&4&l[보스전] &f시련 I 단계 제 %channel% 실험장")
            .loreIB("&f클릭 시 자동 입장 됩니다.")
            .loreIB("&7&m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m")
            .loreIB("&7&l입장 재료: &f &f &f &f &f &f &f &f &f &f &f &f &f &f &7&l보상:")
            .loreIB("&f 상자껍데기 &7x10 &f &f &f &f &f &f &f &f &f &c하드입장권한")
            .loreIB("&7&m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m")
            .loreIB("&7숙련자가 되었다면 도전하세요.")
    val hellButton: ItemStack
        get() = CraftItemStack.asCraftMirror(CraftItemStack.asNMSCopy(ItemStack(Material.SKULL_ITEM, 1, 1))).warpName("지옥")
            .nameIB("&4&l:: 지옥 %channel%채널 :: &c[ &f%current_player% / 50 &c]")
            .loreIB("&f플레이에 매우 숙련된 분들께")
            .loreIB("&f추천드리는 &4고난이도 &f입니다")
            .loreIB("&7&m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m")
            .loreIB("&7&l정보: &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &7&l도움말:")
            .loreIB("&f 난이도: &7■■■ &f &f &f &f &f &f &f &f &f &f &f &f &f[/파티] 권장")
            .loreIB("&f 형태: &7도시 &f &f &f &f &f &f &f &f &f &f &f &f &f [구조지대] 추가")
            .loreIB("&f 날씨: &7해질녘")
            .loreIB("&f")
            .loreIB("&7&l배경:")
            .loreIB("&7머물 곳 잃은 망자들은")
            .loreIB("&7마침내 인간의 땅을 침략하여 차지하였고,", 2)
            .loreIB("&7그들은 지옥을 버리고 이 땅에 자리한다.")
            .loreIB("&7이 땅의 새로운 이름은 지옥이다.")
    val silenceButton: ItemStack
        get() = CraftItemStack.asCraftMirror(CraftItemStack.asNMSCopy(ItemStack(Material.JACK_O_LANTERN))).warpName("사일런스")
            .nameIB("&0&l:: 사일런스 %channel%채널 :: &c[ &f%current_player% / 50 &c]")
            .loreIB("&4해당 시간에 입장함으로써 생기는")
            .loreIB("&4모든 불이익은 본인의 책임입니다.")
            .loreIB("&7&m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m &m")
            .loreIB("&7&l정보: &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &f &7&l도움말:")
            .loreIB("&f 난이도: &4■■■■■ &f &f &f &f &f &f &f &f &f &f &f&f&c입장자제")
            .loreIB("&f 형태: &7도시")
            .loreIB("&f 날씨: &7밤")
            .loreIB("&f")
            .loreIB("&7&l배경:")
            .loreIB("&7인간에 대한 신의 심판은 가혹하다")
            .loreIB("&7그렇기에 지옥을 꽉 차게 만들고 이 망자들은", 2)
            .loreIB("&7갈 곳이 없어 다시 지상으로 돌아와")
            .loreIB("&7살아있는 인간을 사냥하기 시작한다.")
            .loreIB("&4온 바닥을 덮은 선혈들.")
            .loreIB("&4울려퍼지는 괴성.")
            .loreIB("&4도망치는 사람들.")
    val lockedButton: ItemStack
        get() = CraftItemStack.asCraftMirror(CraftItemStack.asNMSCopy(ItemStack(Material.RECORD_5))).warpName("잠김")
            .nameIB("§7§l:: LOCKED ::")
            .loreIB("§f현재 굳건히 잠겨진 상태이다.")
            .loreIB("§f무언가 조건을 충족하면 열릴 것만 같다.", 2)


    private fun ItemStack.warpName(name: String): ItemStack {
        val nmsItemTag = (this as CraftItemStack).handle.tagOrDefault
        nmsItemTag.setString(WARP_NAME_KEY, name)
        return this
    }
    fun ItemStack.channel(channel: Int): ItemStack {
        this.nameIB(this.itemMeta.displayName.replace("%channel%", channel.toString()))
        val nmsItemTag = (this as CraftItemStack).handle.tagOrDefault
        nmsItemTag.setInt(CHANNEL_KEY, channel)
        return this
    }
    fun ItemStack.world(world: String): ItemStack {
        val world = Bukkit.getWorld(world)
        if (world == null) {
            SimplyLog.log(LogLevel.ERROR_LOW, plugin, "[${this.itemMeta.displayName}] World $world 는 없는 월드임")
            return this
        }
        this.nameIB(this.itemMeta.displayName.replace("%current_player%", world.playerCount.toString()))
        val nmsItemTag = (this as CraftItemStack).handle.tagOrDefault
        nmsItemTag.setString(WORLD_NAME_KEY, world.name)
        return this
    }
}