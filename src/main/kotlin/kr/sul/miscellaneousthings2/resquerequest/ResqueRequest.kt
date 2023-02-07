package kr.sul.miscellaneousthings2.resquerequest

import com.shampaggon.crackshot2.events.WeaponShootEvent
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.miscellaneousthings2.helicopter.Helicopter
import kr.sul.servercore.util.MsgPrefix
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import kotlin.random.Random

class ResqueRequest(
    val p: Player,
    private val region: ResqueRegion
): Listener, CrystalBrokenEventListener {
    private val helicopter = Helicopter(
        Helicopter.HelicopterForm.NORMAL_FORM,
        getRandomLocFarFrom(p.location.add(0.0, 20.0, 0.0)),
        p.location.add(0.0, 20.0, 0.0),
        MOVE_AMOUNT_AT_ONE_TICK
    )
    private val task: BukkitTask
    private var passedTime = 0

    init {
        helicopter.spawn()
        p.sendTitle("", "§a§l구조 요청!")
        p.sendMessage("${MsgPrefix.get("RESQUE")}§f구조를 요청합니다. §4§l[ ! ] §f붉은 깃 위의 크리스탈이 파괴될 경우 구조가 §c취소§f됩니다.\n")

        region.register(this)
        val period = 20L
        task = Bukkit.getScheduler().runTaskTimer(plugin, {
            passedTime += period.toInt()
            onEverySecond()
            // 구역 이탈 확인
            if (!region.isWithinThisRegion(p.location)) {
                onGotOutFromZone()
                return@runTaskTimer
            }

            if (passedTime >= TIME_TO_RESQUE) {
                makeToGoSpawn()
            }
        }, 0L, period)
    }
    private fun onEverySecond() {
        p.sendActionBar("구조 까지 §a${(TIME_TO_RESQUE -passedTime)/20}초 §f남았습니다.")
    }
    private fun makeToGoSpawn() {
        p.sendTitle("§a스폰§f으로 이동되었습니다", "")
//                p.teleport()  TODO
        destroy()
    }


    override fun onCrystalBroken() {
        p.sendMessage("${MsgPrefix.get("RESQUE")}§c구조가 취소 되었습니다. (크리스탈 파괴)")
        destroy()
    }
    private fun onGotOutFromZone() {
        p.sendMessage("${MsgPrefix.get("RESQUE")}§c구조가 취소 되었습니다. (구조 영역 이탈)")
        destroy()
    }

    @EventHandler
    fun onShoot(e: WeaponShootEvent) {
        p.sendMessage("${MsgPrefix.get("RESQUE")}§c구조가 취소 되었습니다. (총기 사용)")
        destroy()
    }


    fun destroy() {
        p.sendActionBar("§f")
        task.cancel()
        helicopter.cancel()
        region.unregister(this)
    }


    companion object {
        private const val TIME_TO_RESQUE = 10*20 // tick
        private const val MOVE_AMOUNT_AT_ONE_TICK = 0.3
        private const val FAR_FROM = TIME_TO_RESQUE * MOVE_AMOUNT_AT_ONE_TICK

        private fun getRandomLocFarFrom(loc: Location): Location {
            val randomDirection = Vector(Random.nextDouble(-1.0, 1.0), 0.0, Random.nextDouble(-1.0, 1.0))
            return loc.clone().add(randomDirection.normalize().multiply(FAR_FROM))
        }
    }
}