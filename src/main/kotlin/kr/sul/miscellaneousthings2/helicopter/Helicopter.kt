package kr.sul.miscellaneousthings2.helicopter

import de.slikey.effectlib.util.ParticleEffect
import de.tr7zw.nbtapi.NBTEntity
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import me.sul.parachute.Parachute
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector


// TODO: 헬기, 파티클 렌더링 거리?
class Helicopter(private val helicopterForm: HelicopterForm,
                 val passenger: Player,
                 private val startLoc: Location, private val destinationLoc: Location, private val moveSpeedAtOnce: Double = 0.3,
                 private val whenCanPlayerGetOutVoluntarily: Double = 1.0  // 0.0~1.0: 0.0은 바로 내릴 수 있고, 1.0은 목적지 도착 전까지 중간에 자의적으로 못 내림  (굳이 지금 구현할 필요가 없네?)
) {
    private val headingDirection = destinationLoc.toVector().subtract(startLoc.toVector()).normalize()  // 향하는 방향. .toVector()과 .direction은 다름  (toVector은 0,0,0에서 location까지의 Vector, direction은 그냥 내재된 yaw와 pitch)
    private val totalLength = startLoc.distance(destinationLoc)

    private val helicopter: ArmorStand
    private val camera: ArmorStand

    private val moveTask: BukkitTask

    // 중간에 접속 종료하는 것을 고려
    init {
        if (startLoc.world != destinationLoc.world) throw Exception("startLoc의 world와 destinationLoc의 world가 다름.  $startLoc | $destinationLoc")
        if ((totalLength/moveSpeedAtOnce).toInt() == 0) throw Exception("도착지와의 거리가 너무 짧음.  $startLoc | $destinationLoc | $totalLength | ${passenger.name}")
        RestrictPassenger.helicopterPassengers.add(passenger)


        // yaw, pitch 설정
        startLoc.direction = headingDirection
        destinationLoc.direction = headingDirection

        // 헬기 소환
        val modifiedHelicopterInitialLoc = startLoc.clone()
                .add(Vector(headingDirection.z, 0.0, -headingDirection.x).multiply(0.3))  // headingDirection의 왼쪽 방향으로 약간 더해준 값(아이템이 오른쪽으로 치우쳐져 있기 때문) https://bukkit.org/threads/getting-a-block-to-a-players-right-left.180411/
        helicopter = startLoc.world.spawnEntity(modifiedHelicopterInitialLoc, EntityType.ARMOR_STAND) as ArmorStand
        NBTEntity(helicopter).run { setByte("Invisible", 1); setByte("NoGravity", 1) }  // NBTEntity는 NBTItem과 다르게 item = nbti.item 같은 과정 없어도 알아서 적용됨
        helicopter.equipment.itemInMainHand = helicopterForm.item
        helicopter.rightArmPose = EulerAngle(0.15707, 0.0, 0.0)  // 좌우반전 생각해서 -0.15.. -> +0.15.. 더 숙이면 EulerAngle(-0.26179, 0.0, 0.0).

        // 플레이어, 카메라 세팅
        val cameraLoc = startLoc.clone()
            .add(headingDirection.clone().normalize().multiply(-1).x*6, 7.0, headingDirection.clone().normalize().multiply(-1).z*6)
        cameraLoc.direction = startLoc.toVector().subtract(cameraLoc.toVector())  // 카메라 -> 헬리콥터 방향
        cameraLoc.pitch += 8  // 시선 약간 깔아주기
        camera = startLoc.world.spawnEntity(cameraLoc, EntityType.ARMOR_STAND) as ArmorStand
        NBTEntity(camera).run { setByte("Invisible", 1); setByte("NoGravity", 1) }

        // isHelicopter 태그 달기
        arrayListOf(helicopter, camera).forEach { ent ->
            ent.customName = HelicopterPreventBug.HELICOPTER_TAG
            ent.isCustomNameVisible = false
        }

        passenger.run {
            teleport(startLoc)
            gameMode = GameMode.SPECTATOR
            eyeLocation.direction = headingDirection
            spectatorTarget = camera
        }



        // 헬기 이동시키기
        val totalMovementCnt = (totalLength/moveSpeedAtOnce).toInt()
        val moveVector = headingDirection.clone().normalize()
                .multiply(totalLength/totalMovementCnt)  // 1틱당 이동하는 칸이 moveSpeedAtOnce와 같진 않고 근사값임. (정확히 목표지에 도달하기 위함)

        moveTask = object : BukkitRunnable() {
            var cnt = 0  // 이렇게 되면 일단 run() 1번은 무조건 실행
            override fun run() {
                if (!passenger.isOnline) {
                    cancel()
                    return
                }

                /* // 중도 방출이 가능하게 될 때 딱 한번만 실행
                if (cnt/totalMovementCnt.toDouble() >= whenCanPlayerGetOutVoluntarily
                    && cnt-1/totalMovementCnt.toDouble() < whenCanPlayerGetOutVoluntarily) {
                    ReadyToEjectListener.thisHelicopterIsReadyToEject(this@Helicopter, (totalMovementCnt-cnt))
                }*/

                // 헬리콥터 움직이기
                helicopter.teleport(helicopter.location.add(moveVector))
                helicopter.location.direction = headingDirection
                // 카메라 부드럽게 움직이기 (cnt <= 40)
                val tempCameraLoc = camera.location.add(moveVector)
                if (cnt <= 40) {
                    when {
                        cnt <= 10 -> tempCameraLoc.add(0.0, 0.07, 0.0)
                        cnt <= 20 -> tempCameraLoc.add(0.0, 0.04, 0.0)
                        cnt <= 30 -> tempCameraLoc.add(0.0, 0.02, 0.0)
                        cnt <= 40 -> tempCameraLoc.add(0.0, 0.01, 0.0)
                    }
                }
                camera.teleport(tempCameraLoc)
                passenger.spectatorTarget = camera

                when (cnt) {
                    totalMovementCnt-60-1 -> passenger.sendTitle("", "§c§l3..", 10, 20, 0)
                    totalMovementCnt-40-1 -> passenger.sendTitle("", "§c§l2..", 10, 20, 0)
                    totalMovementCnt-20-1 -> passenger.sendTitle("", "§c§l1..", 10, 20, 0)
                }


                // 가장 마지막엔 강제로 방출하고, Cancel this task
                if (++cnt == totalMovementCnt) {
                    cancel()
                    passenger.sendTitle("", "§c§l낙하!", 10, 20, 10)
                    ejectPassengerFromHelicopter()
                }
            }
        }.runTaskTimer(plugin, 0L, 1L)

        // 헬기 끝났을 때
        object : BukkitRunnable() {
            override fun run() {  // Scheduler
                if (moveTask.isCancelled) {
                    RestrictPassenger.helicopterPassengers.remove(passenger)
//                    ReadyToEjectListener.removeHelicopterFromList(this@Helicopter)

                    // 헬기 사라지는 연출
                    object : BukkitRunnable() {  // Scheduler
                        var cnt = 0
                        override fun run() {
                            cnt++
                            helicopter.teleport(helicopter.location.add(moveVector))
                            if (cnt >= 40) {
                                ParticleEffect.CLOUD.display(1.5.toFloat(), 1.5.toFloat(), 1.5.toFloat(), 0.toFloat(), 100,
                                    helicopter.location.add(0.0, 0.7, 0.0), 50.toDouble())
                            }
                            if (cnt == 60) {
                                // kill helicopter & camera entity
                                helicopter.remove()
                                camera.remove()
                                cancel()
                            }
                        }
                    }.runTaskTimer(plugin, 0L, 1L)
                    cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }



    fun ejectPassengerFromHelicopter() {
        // 헬기 도착 전, 중간에 방출됐을 때를 고려
        if (!moveTask.isCancelled) {
            moveTask.cancel()
        }

        passenger.run {
            gameMode = GameMode.SURVIVAL
            teleport(helicopter.location)
            setMetadata(RestrictPassenger.WAITING_FOR_PARACHUTE_TO_UNFOLD_KEY, FixedMetadataValue(plugin, true))
        }

        // 지면에 가까워지면 자동으로 낙하산 펼치기
        object: BukkitRunnable() {  // Scheduler
            override fun run() {
                // 낙하산 펼치기
                if (HelicopterUtil.getDistanceFromGround(passenger.location) <= WHEN_TO_UNFOLD_PARACHUTE) {
                    cancel()
                    passenger.removeMetadata(RestrictPassenger.WAITING_FOR_PARACHUTE_TO_UNFOLD_KEY, plugin)
                    Parachute.getPlugin().activateParachute(passenger)
                }
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }






/*    // 중도 방출
    object ReadyToEjectListener: Listener {
        private val readyHelicopterList = arrayListOf<Helicopter>()

        fun thisHelicopterIsReadyToEject(helicopter: Helicopter, maxTimeToStayInReady: Int) {
            helicopter.passenger.sendTitle("&6&l[SHIFT] &f낙하하기", "", 4, maxTimeToStayInReady, 0)
        }
        fun removeHelicopterFromList(helicopter: Helicopter) {
            readyHelicopterList.remove(helicopter)
        }

        // 강하 버튼. 근데 Shift가 더 좋지 않을까?
        @EventHandler
        fun onPressF(e: PlayerSwapHandItemsEvent) {
            readyHelicopterList.find { it.passenger == e.player }?.run {
                readyHelicopterList.remove(this)
                passenger.sendTitle("", "", 0, 1, 0)
                ejectPassengerFromHelicopter()
            }
        }
    }*/




    // 헬기 모습 Enum Class
    enum class HelicopterForm(val item: ItemStack) {
        NORMAL_FORM(ItemStack(Material.NETHER_BRICK_ITEM))
    }
    companion object {
        private const val WHEN_TO_UNFOLD_PARACHUTE = 25  // 바닥에서의 거리가 n칸 이하일 때 펼침
    }
}