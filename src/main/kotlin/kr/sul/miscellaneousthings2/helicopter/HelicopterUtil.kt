package kr.sul.miscellaneousthings2.helicopter

import org.bukkit.Location
import org.bukkit.util.Vector

object HelicopterUtil {
    // 도착지에서 distance만큼 떨어진 헬기 시작지점
    fun getLocationApartFrom(destinationLoc: Location, distance: Int): Location {
        val direction = Vector(1.0, 0.0, 2.5).normalize()
        direction.multiply(distance)
        return destinationLoc.clone().add(direction)
    }

    fun getDistanceFromGround(loc: Location): Int {
        val loc = loc.clone()
        val y = loc.blockY
        var distanceFromGround = 0
        for (i in y downTo 0) {
            loc.y = i.toDouble()
            if (loc.block.type.isSolid) break
            distanceFromGround++
        }
        return distanceFromGround
    }
}