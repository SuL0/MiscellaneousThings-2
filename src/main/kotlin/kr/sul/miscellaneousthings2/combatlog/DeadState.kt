package kr.sul.miscellaneousthings2.combatlog

import com.destroystokyo.paper.Title
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.MsgPrefix
import me.filoghost.holographicdisplays.api.beta.HolographicDisplaysAPI
import me.filoghost.holographicdisplays.api.beta.hologram.Hologram
import me.filoghost.holographicdisplays.api.beta.hologram.VisibilitySettings
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask
import org.golde.bukkit.corpsereborn.CorpseAPI.CorpseAPI
import kotlin.random.Random


// DB를 통해서 CombatLog로 죽은 사람은 접속하면 바로 DeadState 되게끔 구현하면 좋을 것 같은데. 거기다가 왜 죽은지도 사유 써주고. HikariDB https://github.com/lucko/helper/blob/master/helper-sql/src/main/java/me/lucko/helper/sql/plugin/HelperSql.java
// https://github.com/filoghost/HolographicDisplays/wiki/Individual-holograms
class DeadState(private val p: Player, private val killer: Entity?, drops: List<ItemStack>, damageTakenReport: DamageTakenReport): Listener {
    private val hologram: Hologram
    private val autoRespawnTask: BukkitTask
    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        // 시체 생성
        // TODO 아이템 뭐 들어가고 갑옷은 입는지 확인
        val corpse = CorpseAPI.spawnCorpse(p, p.name, p.location, drops, Random.nextInt(361))

        // 튕겨나가는 모션
        val direction = run {
            if (killer is Projectile) {
                killer.velocity
            } else if (killer != null) {
                killer.location.toVector().subtract(p.location.toVector())
            } else {
                p.location.add(Random.nextDouble(), 0.5, Random.nextDouble()).toVector().subtract(p.location.toVector())
            }
        }
        p.velocity.add(direction.normalize())
        p.gameMode =
            GameMode.SPECTATOR  // 겜 들어왔을 때 해당 게임모드면 스폰으로 강제 리스폰 시켜야 함?  아님 그냥 CombatLog로 죽은 사람도 SPECTATOR 모드로 하면 안되나

        // 홀로그램 생성
        hologram = HolographicDisplaysAPI.get(plugin).createHologram(corpse.trueLocation.add(0.0, 3.0, 0.0))
        hologram.lines.appendText("처치자: ${p.killer.name}")  // killer은 처치자만 뜨는건지 아니면 그냥 죽인 물체도 포함되는지
        hologram.lines.appendText("받은 몸 관통 횟수 | 데미지: ${damageTakenReport.numOfNormalShot} | ${damageTakenReport.totalDamageOfNormalShot}")
        hologram.lines.appendText("받은 헤드샷 관통 횟수 | 데미지: ${damageTakenReport.numOfHeadShot} | ${damageTakenReport.totalDamageOfHeadShot}")
        hologram.lines.appendText("받은 폭발 데미지 횟수 | 데미지: ${damageTakenReport.numOfExplosion} | ${damageTakenReport.totalDamageOfExplosion}")
        hologram.lines.appendText("받은 기타 데미지 횟수 | 데미지: ${damageTakenReport.numOfEtcAttack} | ${damageTakenReport.totalDamageOfEtc}")
        hologram.lines.appendText("  §f< 확인 완료: §a클릭 §f> §e보상수령 §7(좀비머리 1개)").setClickListener {
            onClickHologram()
        }
        val visibility = hologram.visibilitySettings
        visibility.globalVisibility = VisibilitySettings.Visibility.HIDDEN
        visibility.setIndividualVisibility(p, VisibilitySettings.Visibility.VISIBLE)

        p.sendMessage("${MsgPrefix.get("DEAD")}§e10초 §f후에 자동으로 §a리스폰§f됩니다.")

        autoRespawnTask = Bukkit.getScheduler().runTaskLater(plugin, {
            respawn()
        }, 20*10L)
    }




    @EventHandler(priority = EventPriority.HIGH)
    fun onQuit(e: PlayerQuitEvent) {
        destroy()
    }
    @EventHandler(priority = EventPriority.LOW)
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        if (e.isCancelled) return
        e.isCancelled = true
        e.player.sendMessage("${MsgPrefix.get("DEAD")}§c현재 상태로는 명령어를 이용할 수 없습니다.")
    }
    @EventHandler(priority = EventPriority.LOW)
    fun onTeleport(e: PlayerTeleportEvent) {
        if (e.isCancelled) return
        e.isCancelled = true
        e.player.sendMessage("${MsgPrefix.get("DEAD")}§c현재 상태로는 텔레포트를 이용할 수 없습니다.")
    }

    // 좀비머리 1개 보상 주고 리스폰
    private fun onClickHologram() {
        // TODO 보상 좀비머리 1개
        respawn()
    }

    private fun respawn() {
        destroy()
        p.gameMode = GameMode.SURVIVAL
//        ClassifyWorlds.teleportToSpawn(p)
        p.sendTitle(Title("", "§a§l리스폰 되었습니다!"))
    }


    private fun destroy() {
        HandlerList.unregisterAll(this)
        hologram.delete()
        autoRespawnTask.cancel()
    }
}