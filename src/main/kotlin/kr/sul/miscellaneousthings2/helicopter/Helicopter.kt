package kr.sul.miscellaneousthings2.helicopter

import de.slikey.effectlib.util.ParticleEffect
import de.tr7zw.nbtapi.NBTEntity
import kr.sul.miscellaneousthings2.Main
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector

class Helicopter(
    private val helicopterForm: HelicopterForm,
    private val startLoc: Location,
    private val destinationLoc: Location,
    private val moveAmountAtOneTick: Double = 0.3,
) {

    private val headingDirection = destinationLoc.toVector().subtract(startLoc.toVector()).normalize()  // 향하는 방향. .toVector()과 .direction은 다름  (toVector은 0,0,0에서 location까지의 Vector, direction은 그냥 내재된 yaw와 pitch)
    private val totalLength = startLoc.distance(destinationLoc)
    private lateinit var helicopterArmorStand: ArmorStand

    private var moveTask: BukkitTask? = null

    fun spawn() {
        if (startLoc.world != destinationLoc.world) throw Exception("startLoc의 world와 destinationLoc의 world가 다름.  $startLoc | $destinationLoc")
        if ((totalLength/moveAmountAtOneTick).toInt() == 0) throw Exception("도착지와의 거리가 너무 짧음.  $startLoc | $destinationLoc | $totalLength")

        // yaw, pitch 설정
        startLoc.direction = headingDirection
        destinationLoc.direction = headingDirection

        // 헬기 소환
        val modifiedHelicopterInitialLoc = startLoc.clone()
            .add(Vector(headingDirection.z, 0.0, -headingDirection.x).multiply(0.3))  // headingDirection의 왼쪽 방향으로 약간 더해준 값(아이템이 오른쪽으로 치우쳐져 있기 때문) https://bukkit.org/threads/getting-a-block-to-a-players-right-left.180411/
        helicopterArmorStand = startLoc.world.spawnEntity(modifiedHelicopterInitialLoc, EntityType.ARMOR_STAND) as ArmorStand
        NBTEntity(helicopterArmorStand).run { setByte("Invisible", 1); setByte("NoGravity", 1) }  // NBTEntity는 NBTItem과 다르게 item = nbti.item 같은 과정 없어도 알아서 적용됨
        helicopterArmorStand.equipment.itemInMainHand = helicopterForm.item
        helicopterArmorStand.rightArmPose = EulerAngle(0.15707, 0.0, 0.0)  // 좌우반전 생각해서 -0.15.. -> +0.15.. 더 숙이면 EulerAngle(-0.26179, 0.0, 0.0).

        // isHelicopter 태그 달기
        helicopterArmorStand.customName = HELICOPTER_TAG
        helicopterArmorStand.isCustomNameVisible = false



        // 헬기 이동시키기
        val totalMovementCnt = (totalLength/moveAmountAtOneTick).toInt()
        val moveVector = headingDirection.clone().normalize()
            .multiply(totalLength/totalMovementCnt)  // 1틱당 이동하는 칸이 moveSpeedAtOnce와 같진 않고 근사값임. (정확히 목표지에 도달하기 위함)

        moveTask = object : BukkitRunnable() {
            var cnt = 0  // 이렇게 되면 일단 run() 1번은 무조건 실행
            override fun run() {

                // 헬리콥터 움직이기
                helicopterArmorStand.teleport(helicopterArmorStand.location.add(moveVector))
                helicopterArmorStand.location.direction = headingDirection
            }
        }.runTaskTimer(Main.plugin, 0L, 1L)

        // 헬기 끝났을 때
        object : BukkitRunnable() {
            override fun run() {  // Scheduler
                if (moveTask!!.isCancelled) {
                    // 헬기 사라지는 연출
                    object : BukkitRunnable() {  // Scheduler
                        var cnt = 0
                        override fun run() {
                            cnt++
                            helicopterArmorStand.teleport(helicopterArmorStand.location.add(moveVector))
                            if (cnt >= 40) {
                                ParticleEffect.CLOUD.display(1.5.toFloat(), 1.5.toFloat(), 1.5.toFloat(), 0.toFloat(), 100,
                                    helicopterArmorStand.location.add(0.0, 0.7, 0.0), 50.toDouble())
                            }
                            if (cnt == 60) {
                                // kill helicopter
                                helicopterArmorStand.remove()
                                cancel()
                            }
                        }
                    }.runTaskTimer(Main.plugin, 0L, 1L)
                    cancel()
                }
            }
        }.runTaskTimer(Main.plugin, 0L, 1L)
    }

    fun cancel() {
        moveTask?.cancel()
    }


    // 헬기 모습 Enum Class
    enum class HelicopterForm(val item: ItemStack) {
        NORMAL_FORM(ItemStack(Material.RABBIT_HIDE))
    }




    companion object {
        private const val HELICOPTER_TAG = "HELI: isHelicopter"

        // 서버 시작됐을 때 실행
        init {
            // 서버가 강제종료 됐거나, 오류로 인해서 남은 헬기 아머스탠드를 모두 제거
            Bukkit.getScheduler().runTask(Main.plugin) {
                Bukkit.getWorlds().forEach { world ->
                    world.entities.filterIsInstance<ArmorStand>().forEach { armorStand ->
                        if (armorStand.customName == HELICOPTER_TAG) {
                            armorStand.remove()
                        }
                    }
                }
            }
        }
    }
}