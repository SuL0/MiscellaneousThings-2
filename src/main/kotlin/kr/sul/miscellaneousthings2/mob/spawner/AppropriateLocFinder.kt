package kr.sul.miscellaneousthings2.mob.spawner

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.random.Random

object AppropriateLocFinder {
    private val random = Random

    /**
     * @param p 중심이 되는 플레이어
     * @param blockTypes 랜덤으로 위치 찾을 때, 해당 블럭 위에만 스폰되게끔 찾음
     */
    fun find(p: Player, blockTypes: List<Material>): Location? {
        val centerPoint = p.location

        // 예외 지역 (여기에 포함되면 좀비 소환 시도 자체를 하지 않음)
        if (centerPoint.world.name.startsWith("BeachTown", true)) {
            // BeachTown 검은 큰 빌딩 2층부터
            if (centerPoint.y >= 77
                && 365 <= centerPoint.x && centerPoint.x <= 468
                && 249 <= centerPoint.z && centerPoint.z <= 319) {
                return null
            }
        }

        for (i in 1..20) {
            val tryToFind = tryToFind(centerPoint, blockTypes)
            if (tryToFind != null) {  // tryToFind에서 적절한 위치를 찾았을 때
                // 예외 지역(다시 찾기)
                if (tryToFind.world.name.startsWith("BeachTown", true)) {
                    // 검은 큰 빌딩 옆의 맥도날드
                    if (tryToFind.x in 482.0..502.0
                        && tryToFind.z in 293.0..304.0) {
                        continue
                    }
                }
                return tryToFind
            }
        }

//        SimplyLog.log(LogLevel.ERROR_LOW, plugin,
//            "${centerPoint.blockX}, ${centerPoint.blockY}, ${centerPoint.blockZ} 좌표를 기준으로 근처 $blockTypes 이 있는 장소를 찾을 수 없음")  // 너무 남발되려나?
        return null
    }


    // TODO: 지면의 y를 설정하고, 플레이어가 지면보다 아래에 있으면 blockType을 찾을 때 기준 좌표에서 위로만 찾고, 플레이어가 지면보다 위에 있으면 기준 좌표에서 아래만 찾으면 더 좋을 것 같은데?
    private fun tryToFind(centerPoint: Location, blockTypes: List<Material>): Location? {
        // 랜덤하게 8~15칸 만큼 떨어진 좌표
        val randomLength = random.nextInt(8, 15+1)  // 8~15
        val randomDirVector = Vector(random.nextDouble(-1.0, 1.0), 0.0, random.nextDouble(-1.0, 1.0)).normalize().multiply(randomLength)
        val randomLoc = centerPoint.clone().add(randomDirVector).toBlockLocation()
        if (randomLoc.block.lightFromSky <= 12) {
            return null
        }

        // 기준 좌표에서 위 아래 넓혀나가며 찾는 방식
        val world = centerPoint.world
        for (i in 0..20) {
            // plus
            if (blockTypes.contains(world.getBlockAt(randomLoc.blockX, randomLoc.blockY+i, randomLoc.blockZ).type)) {
                if (world.getBlockAt(randomLoc.blockX, randomLoc.blockY+i+1, randomLoc.blockZ).type == Material.AIR
                        && world.getBlockAt(randomLoc.blockX, randomLoc.blockY+i+2, randomLoc.blockZ).type == Material.AIR) {
                    return randomLoc.add(0.0, i.toDouble(), 0.0)
                }
            }
            // minus
            else if (blockTypes.contains(world.getBlockAt(randomLoc.blockX, randomLoc.blockY-i, randomLoc.blockZ).type)) {
                if (world.getBlockAt(randomLoc.blockX, randomLoc.blockY-i+1, randomLoc.blockZ).type == Material.AIR
                        && world.getBlockAt(randomLoc.blockX, randomLoc.blockY-i+2, randomLoc.blockZ).type == Material.AIR) {
                    return randomLoc.add(0.0, -i.toDouble(), 0.0)
                }
            }
        }
        return null
    }
}