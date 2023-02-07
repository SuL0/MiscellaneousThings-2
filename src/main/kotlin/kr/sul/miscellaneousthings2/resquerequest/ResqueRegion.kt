package kr.sul.miscellaneousthings2.resquerequest

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

enum class ResqueRegion(
    private val loc1: Location,
    private val loc2: Location,
    private val regionCrystal: ResqueRegionCrystal,   // Crystal이 부서지면 Region은 그걸 알아야 할 필요가 있는데? 어떻게 알려주는 게 좋을까
): CrystalBrokenEventListener {
    A(
        Location(Bukkit.getWorld("world"), 1023.0, 96.0, -855.0),
        Location(Bukkit.getWorld("world"), 1030.9, 99.0, -848.9),
        ResqueRegionCrystal(Location(Bukkit.getWorld("world"), 1022.5, 111.5, -856.5))
    ),
    B(
        Location(Bukkit.getWorld("world"), 1240.0, 96.0, -1258.0),
        Location(Bukkit.getWorld("world"), 1248.9, 111.0, -1250.9),
        ResqueRegionCrystal(Location(Bukkit.getWorld("world"), 1040.5, 111.5, -1258.5))
    );
    private val registeredResqueRequestList = arrayListOf<ResqueRequest>()

    init {
        regionCrystal.crystalBrokenEventListener = this
    }

    fun isWithinThisRegion(loc: Location): Boolean {
        return (loc1.x < loc.x && loc.x < loc2.x
                && loc1.y < loc.y && loc.y < loc2.y
                && loc1.z < loc.z && loc.z < loc2.z)
    }

    fun isInRegistered(p: Player): Boolean {
        return (registeredResqueRequestList.find { it.p == p } != null)
    }
    fun register(resqueRequest: ResqueRequest) {
        registeredResqueRequestList.add(resqueRequest)
        if (regionCrystal.isDead()) {
            regionCrystal.spawn()
        }
    }
    fun unregister(resqueRequest: ResqueRequest) {
        registeredResqueRequestList.remove(resqueRequest)
        if (registeredResqueRequestList.size == 0) {
            regionCrystal.despawn()
        }
    }

    override fun onCrystalBroken() {
        registeredResqueRequestList.forEach {
            it.onCrystalBroken()
        }
    }





    companion object {
        fun findRegionGivenLocIsWithin(loc: Location): ResqueRegion? {
            values().forEach { resqueRegion ->
                if (resqueRegion.isWithinThisRegion(loc)) return resqueRegion
            }
            return null
        }
    }
}